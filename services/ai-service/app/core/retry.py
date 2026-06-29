from collections.abc import Callable
from typing import TypeVar

from tenacity import retry, retry_if_exception_type, stop_after_attempt, wait_exponential_jitter

T = TypeVar("T")


def provider_retry(max_attempts: int) -> Callable[[Callable[..., T]], Callable[..., T]]:
    return retry(
        stop=stop_after_attempt(max_attempts),
        wait=wait_exponential_jitter(initial=0.5, max=4),
        retry=retry_if_exception_type((TimeoutError, ConnectionError)),
        reraise=True,
    )
