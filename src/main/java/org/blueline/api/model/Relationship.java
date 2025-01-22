package org.blueline.api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.blueline.api.model.enums.RequestStatus;

@Entity
@Table(name = "relationships")
@Data
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_asker_id", nullable = false)
    private User userAsker;

    @ManyToOne
    @JoinColumn(name = "user_receiver_id", nullable = false)
    private User userReceiver;

    @Column(name = "request_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

}
