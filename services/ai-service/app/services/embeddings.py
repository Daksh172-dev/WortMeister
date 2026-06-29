import math
from functools import lru_cache

import numpy as np

try:
    import faiss
except Exception:  # pragma: no cover
    faiss = None

try:
    from sentence_transformers import SentenceTransformer
except Exception:  # pragma: no cover
    SentenceTransformer = None


class EmbeddingService:
    def __init__(self, model_name: str) -> None:
        self.model_name = model_name

    @lru_cache(maxsize=1)
    def _model(self):
        if SentenceTransformer is None:
            return None
        return SentenceTransformer(self.model_name)

    def embed(self, texts: list[str]) -> list[list[float]]:
        model = self._model()
        if model is not None:
            vectors = model.encode(texts, normalize_embeddings=True).tolist()
            return [[float(v) for v in vector] for vector in vectors]
        return [self._fallback_vector(text) for text in texts]

    def similar(self, word: str, candidates: list[str], limit: int) -> list[dict[str, float | str]]:
        vectors = np.array(self.embed([word, *candidates]), dtype="float32")
        query = vectors[:1]
        corpus = vectors[1:]
        if faiss is not None:
            index = faiss.IndexFlatIP(corpus.shape[1])
            index.add(corpus)
            scores, indexes = index.search(query, min(limit, len(candidates)))
            return [
                {"word": candidates[int(idx)], "score": float(score)}
                for score, idx in zip(scores[0], indexes[0], strict=False)
            ]
        scores = corpus @ query[0]
        order = np.argsort(scores)[::-1][:limit]
        return [{"word": candidates[int(i)], "score": float(scores[int(i)])} for i in order]

    def _fallback_vector(self, text: str) -> list[float]:
        buckets = [0.0] * 64
        for index, char in enumerate(text.lower()):
            buckets[(ord(char) + index) % len(buckets)] += 1.0
        norm = math.sqrt(sum(v * v for v in buckets)) or 1.0
        return [v / norm for v in buckets]
