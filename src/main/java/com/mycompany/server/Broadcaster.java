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
    public static final BlockingQueue<MessageContainer> queue = new LinkedBlockingQueue<>();
    public static final Set<ClientHandler> clientes = ConcurrentHashMap.newKeySet();
    private static final Gson gson = new Gson();
    
    @Override
    public void run()
    {
        while(true)
        {
            for(var message : queue)
                sendMessage(message);
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
    public static void AddMessage(MessageContainer message){
        queue.add(message);
    }
    public static void RemoveUser(ClientHandler user)
    {
        clientes.remove(user);
    }

}
