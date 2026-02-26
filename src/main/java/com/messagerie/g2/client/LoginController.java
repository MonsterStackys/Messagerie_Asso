package com.messagerie.g2.client;

import com.messagerie.g2.model.User;
import com.messagerie.g2.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserRepository userRepository = new UserRepository();

    /**
     * Gère l'action du bouton "Se connecter"
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // RG7 : Le contenu ne doit pas être vide
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try {
            // RG2 : L'utilisateur doit être authentifié pour accéder aux messages
            User user = userRepository.findByUsername(username);

            // RG9 : Comparaison du mot de passe (à hacher plus tard pour la sécurité)
            if (user != null && user.getPassword().equals(password)) {

                // RG4 : À la connexion, le statut devient ONLINE
                userRepository.updateStatus(user.getId(), User.Status.ONLINE);

                // Chargement de l'interface de discussion
                loadChatWindow(user, event);

            } else {
                showError("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            // RG10 : Affichage d'une erreur en cas de problème réseau/DB
            showError("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge la scène chat.fxml et passe les données de l'utilisateur
     */
    private void loadChatWindow(User user, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur du chat pour lui envoyer l'utilisateur connecté
            ChatController chatController = loader.getController();
            chatController.initData(user);

            // Changement de fenêtre
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Messagerie G2 - " + user.getUsername() + " (" + user.getRole() + ")");
            stage.show();

        } catch (IOException e) {
            showError("Erreur lors du chargement de la fenêtre de chat.");
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setText(message);
    }
}