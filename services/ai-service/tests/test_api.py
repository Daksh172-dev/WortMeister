import pytest
from fastapi.testclient import TestClient

import app.services.embeddings as embedding_module
from app.main import app


HEADERS = {"X-Internal-Api-Key": "local-ai-internal-key"}


@pytest.fixture()
def client(monkeypatch: pytest.MonkeyPatch) -> TestClient:
    monkeypatch.setattr(embedding_module, "SentenceTransformer", None)
    with TestClient(app) as test_client:
        yield test_client


def learner_context() -> dict:
    return {
        "learner_id": "learner-1",
        "cefr_level": "A1",
        "native_language": "en",
        "strengths": ["greetings"],
        "weaknesses": ["articles"],
        "recent_words": ["Tisch"],
    }


def test_health(client: TestClient) -> None:
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "UP"


def test_internal_auth_required(client: TestClient) -> None:
    response = client.post(
        "/internal/v1/vocabulary/extract",
        json={"text": "Der Tisch ist frei.", "context": learner_context()},
    )
    assert response.status_code == 401


def test_vocabulary_extraction(client: TestClient) -> None:
    response = client.post(
        "/internal/v1/vocabulary/extract",
        headers=HEADERS,
        json={"text": "Der Tisch und die Lampe sind im Zimmer.", "context": learner_context()},
    )
    assert response.status_code == 200
    assert response.json()["terms"]


def test_adaptive_difficulty(client: TestClient) -> None:
    response = client.post(
        "/internal/v1/learning/adaptive-difficulty",
        headers=HEADERS,
        json={"context": learner_context(), "recent_scores": [92, 88, 95], "current_difficulty": 3},
    )
    assert response.status_code == 200
    assert response.json()["recommended_difficulty"] == 4


def test_embeddings_and_similarity(client: TestClient) -> None:
    embedding_response = client.post(
        "/internal/v1/embeddings",
        headers=HEADERS,
        json={"texts": ["Tisch", "Stuhl"]},
    )
    assert embedding_response.status_code == 200
    assert embedding_response.json()["dimensions"] > 0

    similar_response = client.post(
        "/internal/v1/words/similar",
        headers=HEADERS,
        json={"word": "Tisch", "candidates": ["Stuhl", "laufen", "essen"], "limit": 2},
    )
    assert similar_response.status_code == 200
    assert len(similar_response.json()["matches"]) == 2
