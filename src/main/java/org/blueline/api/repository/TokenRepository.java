package org.blueline.api.repository;

import org.blueline.api.model.OAuth2Token;
import org.blueline.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<OAuth2Token, Long> {
    Optional<OAuth2Token> findByUserAndProvider(User user, String provider);
}
