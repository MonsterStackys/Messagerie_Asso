package com.messagerie.g2.repository;

import com.messagerie.g2.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class UserRepository {
    // Utilisation du nom défini dans votre persistence.xml
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("messageriePU");

    /**
     * Enregistre un nouvel utilisateur en base de données.
     */
    public void saveUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Recherche un utilisateur par son nom unique pour la connexion.
     * @return User si trouvé, null sinon.
     */
    public User findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Utilisateur non trouvé
        } finally {
            em.close();
        }
    }

    /**
     * Met à jour le statut (ONLINE/OFFLINE) de l'utilisateur.
     */
    public void updateStatus(Long userId, User.Status status) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = em.find(User.class, userId);
        if (user != null) {
            user.setStatus(status);
        }
        em.getTransaction().commit();
        em.close();
    }
}