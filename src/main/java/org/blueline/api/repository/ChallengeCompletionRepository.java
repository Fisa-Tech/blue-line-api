package org.blueline.api.repository;

import java.util.List;

import org.blueline.api.model.challenge.Challenge;
import org.blueline.api.model.challenge.ChallengeCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeCompletionRepository extends JpaRepository<ChallengeCompletion, Long> {
    List<ChallengeCompletion> findByChallenge(Challenge challenge);
}
