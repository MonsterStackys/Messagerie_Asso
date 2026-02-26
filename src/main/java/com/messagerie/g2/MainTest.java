package com.messagerie.g2;

import com.messagerie.g2.model.User;
import com.messagerie.g2.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainTest {
    public static void main(String[] args) {
        // 1. Initialisation de la persistence
        // "messageriePU" doit correspondre au name dans votre persistence.xml
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("messageriePU");
        UserRepository userRepository = new UserRepository();

        try {
            System.out.println("--- Démarrage du test de persistence ---");

            // 2. Vérification/Création d'un utilisateur de test (RG1, RG9)
            // On vérifie si l'utilisateur existe déjà pour éviter les erreurs d'unicité (RG1)
            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("123"); // Mot de passe en clair pour le moment (RG9 à venir)
                admin.setRole(User.Role.ORGANISATEUR); // RG13
                admin.setStatus(User.Status.OFFLINE); // RG4

                userRepository.saveUser(admin);
                System.out.println("✅ Utilisateur 'admin' créé avec succès !");
            } else {
                System.out.println("ℹ️ L'utilisateur 'admin' existe déjà en base.");
            }

            // 3. Test de récupération (RG2)
            User found = userRepository.findByUsername("admin");
            if (found != null) {
                System.out.println("✅ Test de récupération réussi : " + found.getUsername() + " est " + found.getRole());
            }

            System.out.println("🚀 Connexion réussie et tables synchronisées avec pgAdmin !");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test : " + e.getMessage());
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }
}