package com.WakaRights.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Instant createdAt;

    @PrePersist
    void onCreate() { createdAt = Instant.now(); }

    // getters & setters
}
