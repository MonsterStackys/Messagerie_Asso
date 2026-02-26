package com.messagerie.g2.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du fichier FXML de la fenêtre de connexion
            // Le chemin part de la racine de 'src/main/resources'
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            // Configuration de la scène
            Scene scene = new Scene(root);

            // Paramètres de la fenêtre principale
            primaryStage.setTitle("Messagerie Asso G2 - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Optionnel : empêche de redimensionner le login
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erreur critique : Impossible de charger le fichier login.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Méthode appelée lors de la fermeture de l'application.
     * Utile pour fermer proprement les connexions Sockets ou Hibernate.
     */
    @Override
    public void stop() {
        System.out.println("Fermeture de l'application...");
        // Ici, vous pourrez ajouter la déconnexion forcée de l'utilisateur (RG4)
        System.exit(0);
    }

    public static void main(String[] args) {
        // Lance l'application JavaFX
        launch(args);
    }
}