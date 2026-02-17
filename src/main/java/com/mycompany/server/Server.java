package com.mycompany.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) 
    {
        Thread broadcast = new Thread(new Broadcaster());
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("ServerSocket: " + serverSocket);
            
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                
                ClientHandler handler = new ClientHandler(socket);
                
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Server Error:\n" + e);
        }
    }
}
