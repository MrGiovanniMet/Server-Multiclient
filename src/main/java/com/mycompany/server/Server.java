package com.mycompany.server;

import com.mycompany.server.MessageService.BQBroadcast;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static ServerSocket serverSocket;
    public static void main(String[] args) 
    {
        var bc = new BQBroadcast();
        Thread broadcast = new Thread(bc);
        broadcast.start();
        try {
            serverSocket = new ServerSocket(5555);
            System.out.println("ServerSocket: " + serverSocket);
            
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                
                ClientHandler handler = new ClientHandler(socket,bc);
                
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Server Error:\n" + e);
            CloseServer();
        }
    }
    private static void CloseServer(){
        try{
            if (serverSocket!=null){
                serverSocket.close();
            }
        }catch(IOException e){
            System.out.println("Server Error:\n" + e);
        }
        System.out.println("El servidor se ha cerrado");

    }
}
