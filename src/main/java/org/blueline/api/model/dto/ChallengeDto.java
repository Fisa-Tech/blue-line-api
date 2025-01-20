package org.blueline.api.model.dto;

import java.time.LocalDateTime;

import org.blueline.api.model.challenge.ChallengeStatus;
import org.blueline.api.model.challenge.ChallengeType;
import org.blueline.api.model.challenge.StreakPeriod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDto {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id; 
    @JsonProperty(access = Access.READ_ONLY)
    private Long eventId;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ChallengeType type;
    private Double distanceGoal;
    private Long timeGoal;
    private StreakPeriod streakPeriod;
    private Integer streakNbOfParticipations;
    private ChallengeStatus status;
}