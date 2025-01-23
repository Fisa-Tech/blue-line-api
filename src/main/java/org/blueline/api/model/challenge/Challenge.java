package org.blueline.api.model.challenge;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.blueline.api.model.Event;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "challenge")
@Data
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation optionnelle vers un Event
    @ManyToOne(optional = true)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;

    // Type de challenge : DISTANCE ou TIME
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    // Objectif distance (en km). Null si type = TIME
    @Column(name="distance_goal")
    private Double distanceGoal;

    // Objectif temps (en secondes). Null si type = DISTANCE
    @Column(name = "time_goal")
    private Long timeGoal;

    // Périodicité du streak (facultatif), ex: DAY, WEEK, ...
    @Enumerated(EnumType.STRING)
    @Column(name = "streak_period")
    private StreakPeriod streakPeriod;

    // Nombre de participations requises dans la période définie pour valider le streak
    @Column(name = "streak_nb_of_participations")
    private Integer streakNbOfParticipations;

    // État du défi : A_RELEVER, EN_COURS, TERMINE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status;

    // Liste des complétions (résultats) des participants
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChallengeCompletion> completions = new HashSet<>();

    public void addCompletion(ChallengeCompletion completion) {
        completions.add(completion);
        completion.setChallenge(this);
    }
}
