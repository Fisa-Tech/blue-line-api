package org.blueline.api.repository;

import java.util.List;

import org.blueline.api.model.Event;
import org.blueline.api.model.challenge.Challenge;
import org.blueline.api.model.challenge.ChallengeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByStatus(ChallengeStatus status);
    List<Challenge> findByEvent(Event event);
}