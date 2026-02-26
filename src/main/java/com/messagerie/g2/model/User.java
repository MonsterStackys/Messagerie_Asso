package com.messagerie.g2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique auto-incrémenté

    @Column(unique = true, nullable = false)
    private String username; // Nom d'utilisateur unique (RG1)

    @Column(nullable = false)
    private String password; // Mot de passe haché (RG9)

    @Enumerated(EnumType.STRING)
    private Role role; // ORGANISATEUR, MEMBRE ou BENEVOLE

    @Enumerated(EnumType.STRING)
    private Status status; // ONLINE ou OFFLINE (RG4)

    private LocalDateTime dateCreation; // Date d'inscription

    // Énumérations pour la cohérence des données
    public enum Role { ORGANISATEUR, MEMBRE, BENEVOLE }
    public enum Status { ONLINE, OFFLINE }

    // Constructeur par défaut requis par JPA
    public User() {}

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        this.status = Status.OFFLINE; // Par défaut à l'inscription
    }

    // Getters et Setters (indispensables pour Hibernate)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}