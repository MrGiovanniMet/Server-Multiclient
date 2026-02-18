/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server.MessageService;

import com.google.gson.Gson;
import com.mycompany.server.Json.ClientLogInData;
import com.mycompany.server.Json.Message;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author claude
 */
public class BQBroadcast implements Runnable, IBroadcast
{
    private final BlockingQueue<Message> queue;
    private final Map<ClientLogInData,Writer> users;
    private final Gson gson;
    
    
    public BQBroadcast()
    {
        this.queue = new LinkedBlockingQueue<>();
        this.gson = new Gson();
        this.users = new ConcurrentHashMap<>();
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
    private void sendMessage(Message message)
    {
        for(ClientLogInData user : users.keySet())
        {
            if(!user.userName().equals(message.userName()))
            {
                try{
                    this.users.get(user).write(gson.toJson(message));
                }catch(IOException e){
                    System.out.println("System error:\n"+e);
                }
            }
        }
    }
    @Override
    public boolean AddUser(ClientLogInData user,Writer writer)
    {
        if(this.CheckUser(user))
            return false;
        this.users.put(user, writer);
        return true;
    }
    @Override
    public boolean CheckUser(ClientLogInData user)
    { 
       return this.users.containsKey(user);
    }
    @Override
    public boolean RemoveUser(ClientLogInData user)
    {
        if(!this.CheckUser(user))
            return false;
        this.users.remove(user);
        return true;
    }
    @Override
    public void AddMessage(Message message){
        queue.offer(message);
    }
}
