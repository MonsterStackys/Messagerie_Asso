package com.messagerie.g2.server;

import com.messagerie.g2.model.Message;
import com.messagerie.g2.model.User;
import com.messagerie.g2.repository.UserRepository;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private User currentUser;
    private final UserRepository userRepository = new UserRepository();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (socket.isConnected()) {
                Object request = in.readObject();

                if (request instanceof User) { // Première connexion : identification
                    this.currentUser = (User) request;
                    ChatServer.connectedClients.put(currentUser.getUsername(), this);
                    System.out.println("[" + LocalDateTime.now() + "] " + currentUser.getUsername() + " est connecté."); // RG12
                }
                else if (request instanceof Message) {
                    handleIncomingMessage((Message) request);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client déconnecté.");
        } finally {
            closeEverything();
        }
    }

    private void handleIncomingMessage(Message msg) {
        // RG7 : Validation de la taille
        if (msg.getContenu().length() > 1000) return;

        System.out.println("[" + LocalDateTime.now() + "] Message de " + msg.getSender().getUsername()); // RG12

        // Chercher si le destinataire est en ligne
        ClientHandler receiverHandler = ChatServer.connectedClients.get(msg.getReceiver().getUsername());

        if (receiverHandler != null) {
            // RG5 : Envoi en temps réel
            receiverHandler.sendToClient(msg);
        } else {
            // RG6 : Destinataire hors ligne -> Le message est déjà censé être persisté via Hibernate si besoin
            System.out.println("Destinataire hors ligne, message enregistré.");
        }
    }

    public void sendToClient(Message msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeEverything() {
        if (currentUser != null) {
            ChatServer.connectedClients.remove(currentUser.getUsername());
            userRepository.updateStatus(currentUser.getId(), User.Status.OFFLINE); // RG4
        }
        try {
            socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}