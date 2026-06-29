package com.wortmeister.identity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimeTokenRepository extends JpaRepository<OneTimeToken, Long> {
    Optional<OneTimeToken> findByTokenHash(String tokenHash);
}
