package org.blueline.api.model.challenge;
import java.time.LocalDateTime;

import org.blueline.api.model.Event;
import org.blueline.api.model.User;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "challenge_completion")
@Data
public class ChallengeCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Distance parcourue si type DISTANCE
    @Column(name = "distance_achieved")
    private Double distanceAchieved;

    // Temps réalisé si type TIME (en secondes)
    @Column(name = "time_achieved")
    private Long timeAchieved;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    public void setChallenge(Challenge challenge2) {
        this.challenge = challenge2;
    }

    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
