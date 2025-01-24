package org.blueline.api.model;

import org.blueline.api.model.enums.Gender;
import org.blueline.api.model.enums.Status;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "friend_id", unique = true)
    private String friendId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "avatar")
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @ManyToMany(mappedBy = "participants")
    private Set<Event> joinedEvents = new HashSet<>();
}
