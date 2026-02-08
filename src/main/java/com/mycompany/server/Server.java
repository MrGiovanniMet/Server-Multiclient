
package com.mycompany.server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;


public class Server{
    // necesitamos esto para saber que clientes estan activos su nombre y DOS
    
    public static List<Cliente> clientes = new CopyOnWriteArrayList<>();

    public static BlockingQueue<Mensaje> cola = new LinkedBlockingQueue<>();
    
    public static void main(String[] args) {
        try{
            //hola
            
            //creamos el socket
            ServerSocket serverSocket = new ServerSocket(2555);
            System.out.println("ServerSocket: " + serverSocket);
            new Broadcast().start();
            while(true){
                Socket socket = null;
                
                try{
                    // aceptamos 
                    socket = serverSocket.accept();
                    System.out.println("A new Client is connected: " + socket);
                    
                    
                    // creamos la entrada de la comunicacion 
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Assigning new Thread for this Client.");
                    
                    // creamos un hilo de recibir mensajes
                    Thread input = new Recibe(socket,dis,dos);
                    
                    //Thread output = new Manda(socket, dos);
                    // start
                   // output.start();
                   
                   
                    input.start();
                }catch(Exception e){
                    socket.close();
                    System.out.println("Server Error: " + e);
                }
            }
        }catch(IOException e){
            System.out.println("Server Error: " + e);
        }
    }
}
