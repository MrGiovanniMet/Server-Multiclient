/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author claude
 */
public class Broadcaster implements Runnable 
{
    private static final BlockingQueue<MessageContainer> queue;
    private static final Set<ClientHandler> clientes;
    private static final Gson gson;
    
    static
    {
        queue = new LinkedBlockingQueue<>();
        clientes = ConcurrentHashMap.newKeySet();
        gson = new Gson();
    }
    
    
    @Override
    public void run()
    {
        while(true)
        {
            try{
                sendMessage(queue.take());
            }catch(InterruptedException e){
                System.out.println("System error:\n"+e);
            }
        }
    }
    private void sendMessage(MessageContainer message)
    {
        for(var cliente : clientes)
        {
            if(!cliente.equals(message.from()))
            {
                var writer = cliente.getWriter();
                try{
                    writer.write(gson.toJson(message.message()));
                }catch(IOException e){
                    System.out.println("System error:\n"+e);
                }
            }
        }
    }
    public static boolean AddUser(ClientHandler user)
    {
        return clientes.add(user);
    }
    public static boolean CheckUser(ClientHandler user)
    { 
       return clientes.contains(user);
    }
    public static void AddMessage(MessageContainer message){
        queue.offer(message);
    }
    public static boolean RemoveUser(ClientHandler user)
    {
        return clientes.remove(user);
    }

}
