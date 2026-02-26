package com.messagerie.g2.client;

import com.messagerie.g2.model.Message;
import com.messagerie.g2.model.User;
import javafx.application.Platform;
import java.io.*;
import java.net.Socket;

public class NetworkClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChatController chatController;

    public NetworkClient(String host, int port, ChatController controller) {
        this.chatController = controller;
        try {
            this.socket = new Socket(host, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());

            // Lancement d'un thread pour écouter les messages du serveur (RG11/RG10)
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            System.err.println("Impossible de se connecter au serveur : " + e.getMessage());
        }
    }

    private void listenForMessages() {
        try {
            while (socket.isConnected()) {
                Object received = in.readObject();
                if (received instanceof Message) {
                    Message msg = (Message) received;
                    // Mise à jour de l'UI (RG8 : affichage chronologique)
                    Platform.runLater(() -> chatController.displayMessage(
                            msg.getSender().getUsername() + ": " + msg.getContenu()
                    ));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connexion perdue avec le serveur (RG10).");
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}