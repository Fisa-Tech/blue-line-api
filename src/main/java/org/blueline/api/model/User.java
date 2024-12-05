package org.blueline.api.model;

import org.blueline.api.model.enums.Sex;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    public void setIsAdmin(Boolean is_admin){
        this.isAdmin = is_admin;
    }
}
