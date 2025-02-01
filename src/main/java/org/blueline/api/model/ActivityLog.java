package org.blueline.api.model;
import jakarta.persistence.*;
import lombok.Data;
import org.blueline.api.model.enums.UserAction;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAction action;

    @Column(nullable = false)
    private LocalDateTime timestamp;

}

