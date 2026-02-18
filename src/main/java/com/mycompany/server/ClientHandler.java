/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import com.mycompany.server.DataBase.AuthService;
import com.mycompany.server.DataBase.MySQLHandler;
import com.mycompany.server.Json.AuthResponse;
import com.mycompany.server.Json.ClientLogInData;
import com.google.gson.Gson;
import com.mycompany.server.Json.Message;
import com.mycompany.server.MessageService.IBroadcast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author claude
 */
public class ClientHandler implements Runnable 
{
    
    private final Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private final Gson gson = new Gson();
    private ClientLogInData user = new ClientLogInData();
    private final IBroadcast broadCast;
    
    public ClientHandler(Socket socket,IBroadcast broadcast)
    {
        this.socket = socket;
        this.broadCast=broadcast;
        try{
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch(IOException e){
            System.out.println("System error:\n"+e);
            this.CloseSession();
        }
    }
    
    @Override
    public void run()
    {
        this.LogIn();
        String message;
        while(!this.socket.isClosed())
        {
            try{
                message = this.reader.readLine();
            }catch(IOException e){
                System.out.println("System error:\n"+e);
                this.CloseSession();
                break;
            }
            var msg = new Message(this.user.userName(),message);
            Broadcast(msg);
            System.out.println(msg.userName()+": "+msg.message());
        }
    }
    
    private void LogIn()
    {
        AuthResponse res;
        do{
            try{
                ClientLogInData data = gson.fromJson(this.reader.readLine(), ClientLogInData.class);
                res = ValidateUser(data);
                this.writer.write(gson.toJson(res));
                this.writer.newLine();
            }catch(IOException e){
                System.out.println("System error:\n"+e);
                this.CloseSession();
                return;
            }
        }while(!res.Valid());
        this.WelcomeMessage();
    }
    
    private AuthResponse ValidateUser(ClientLogInData data)
    {
        if(this.broadCast.CheckUser(data))
            return new AuthResponse(false,"Lo sentimos, este usuario ya se encuentra adentro");
        return CheckDB(data);
    }
    
    private AuthResponse CheckDB(ClientLogInData data)
    {
        AuthService authService = new AuthService(MySQLHandler.getConnection());
        AuthResponse auth = new AuthResponse();
        try{
            auth = authService.ValidateUser(data);
        }catch(SQLException e){
            System.out.println("System error:\n"+e);
        }finally
        {
            if(auth.Valid())
            {
                this.user=data;
                this.broadCast.AddUser(this.user,this.writer);
            }
        }
        return auth;
    }
    
    private void Broadcast(Message message)
    {
        this.broadCast.AddMessage(message);
    }
    private void WelcomeMessage()
    {
        String mensaje = this.user.userName()+" HA ENTRADO AL CHAT";
        this.Broadcast(new Message("SERVIDOR",mensaje));    
        System.out.println("SERVIDOR: "+mensaje);
    }
    private void CloseSession()
    {
        this.broadCast.RemoveUser(this.user);
        var msg = new Message("SERVIDOR",this.user.userName()+" HA ABANDONADO EL CHAT");
        this.Broadcast(msg);
        System.out.println(msg.userName()+": "+msg.message());
        try{
            if(this.reader != null){
                this.reader.close();
            }
            if(this.writer!= null){
                this.writer.close();
            }
            if(socket!= null){
                socket.close();
            }
        }catch(IOException e){
            System.out.println("System error:\n"+e);
        }
    }
}
