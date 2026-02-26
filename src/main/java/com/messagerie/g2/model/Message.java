package com.messagerie.g2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique auto-incrémenté

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // Utilisateur expéditeur

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // Utilisateur destinataire

    @Column(length = 1000, nullable = false)
    private String contenu; // Texte du message (max 1000 caractères)

    private LocalDateTime dateEnvoi; // Date et heure d'envoi

    @Enumerated(EnumType.STRING)
    private MessageStatus statut; // ENVOYE, RECU ou LU

    public enum MessageStatus { ENVOYE, RECU, LU }

    public Message() {}

    @PrePersist
    protected void onSend() {
        this.dateEnvoi = LocalDateTime.now();
        this.statut = MessageStatus.ENVOYE;
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public MessageStatus getStatut() { return statut; }
    public void setStatut(MessageStatus statut) { this.statut = statut; }
}