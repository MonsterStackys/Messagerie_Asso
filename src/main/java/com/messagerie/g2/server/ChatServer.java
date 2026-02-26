package com.messagerie.g2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ChatServer {
    private ServerSocket serverSocket;
    // Stocke les gestionnaires de clients par leur nom d'utilisateur
    public static Map<String, ClientHandler> connectedClients = new ConcurrentHashMap<>();

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[" + LocalDateTime.now() + "] SERVEUR DÉMARRÉ sur le port " + port); // RG12
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler); // RG11
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChatServer(1234).start();
    }
}