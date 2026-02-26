package com.messagerie.g2.client;

import com.messagerie.g2.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ChatController {
    @FXML private ListView<String> userListView;
    @FXML private VBox messageContainer;
    @FXML private TextField messageField;

    private User currentUser;

    public void initData(User user) {
        this.currentUser = user;
        // Charger ici la liste des membres (RG13)
        userListView.getItems().add("Général (Public)");
    }

    @FXML
    private void handleSendMessage() {
        String text = messageField.getText();

        // RG7 : Le contenu ne doit pas être vide et max 1000 caractères
        if (!text.isEmpty() && text.length() <= 1000) {
            displayMessage("Moi: " + text);
            messageField.clear();
            // TODO : Envoyer l'objet Message au serveur via le Socket
        } else if (text.length() > 1000) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Message trop long (max 1000 caractères).");
            alert.show();
        }
    }

    public void displayMessage(String message) {
        // Platform.runLater est crucial pour mettre à jour l'UI depuis un thread secondaire
        Platform.runLater(() -> {
            messageContainer.getChildren().add(new Label(message));
        });
    }
}