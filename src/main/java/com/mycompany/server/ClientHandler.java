/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import com.mycompany.server.Json.AuthResponse;
import com.mycompany.server.Json.ClientLogInData;
import com.google.gson.Gson;
import com.mycompany.server.Json.Message;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private ClientLogInData data = new ClientLogInData();
    private static final String QUERY ="SELECT user, pass FROM usuario WHERE user = ? AND pass = ?";
    
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
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
            var msg = new Message(this.data.userName(),message);
            Broadcast(msg);
            System.out.println(msg.userName()+": "+msg.message());
        }
    }
    
    private void LogIn()
    {
        AuthResponse res;
        do{
            try{
                this.data = gson.fromJson(this.reader.readLine(), ClientLogInData.class);
                res = ValidateUser(this.data);
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
    
    private AuthResponse ValidateUser(ClientLogInData data){
        Connection conn = MySQLHandler.getConnection();
        AuthResponse auth = new AuthResponse();
        try{
            PreparedStatement ps = conn.prepareStatement(QUERY);
            ps.setString(1, data.userName());
            ps.setString(2, data.password());
            ResultSet rs = ps.executeQuery();
            
            ClientLogInData client = new ClientLogInData(rs.getString("user"),
                    rs.getString("pass"));
            if(rs.next())
            {
                System.out.println(client);
                if(Broadcaster.clientes.contains(this))
                {
                    auth = new AuthResponse(false,
                            "Lo sentimos, este usuario ya se encuentra adentro");
                }else
                {
                    auth = new AuthResponse(true,
                            "Inicio de sesion exitoso");
                    Broadcaster.clientes.add(this);
                }
            }else
            {
                auth = new AuthResponse(false,
                            "usuario o contrasena incorrecta");
            }
        }catch(SQLException e){
            System.out.println("System error:\n"+e);
        }
        return auth;
    }
    private void Broadcast(Message message)
    {
        Broadcaster.AddMessage(new MessageContainer(this,message));
    }
    private void WelcomeMessage()
    {
        String mensaje = this.data.userName()+" HA ENTRADO AL CHAT";
        this.Broadcast(new Message("SERVIDOR",mensaje));    
        System.out.println("SERVIDOR: "+mensaje);
    }
    private void CloseSession()
    {
        Broadcaster.RemoveUser(this);
        var msg = new Message("SERVIDOR",this.data.userName()+" HA ABANDONADO EL CHAT");
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
    @Override
    public int hashCode(){
        return this.data.hashCode();
    }
    @Override
    public boolean equals(Object o)
    {
        if(this==o)return true;
        if(o==null || this.getClass()!=o.getClass())return false;
        ClientHandler c = (ClientHandler)o;
        return this.data.equals(c.data);
    }
    public BufferedWriter getWriter()
    {
        return new BufferedWriter(this.writer);
    }
}
