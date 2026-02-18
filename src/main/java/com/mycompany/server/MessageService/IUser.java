/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.server.MessageService;

import com.mycompany.server.ClientHandler;
import com.mycompany.server.Json.ClientLogInData;
import java.io.Writer;

/**
 *
 * @author claude
 */
public interface IUser {
    public boolean AddUser(ClientLogInData user,Writer writer);
    public boolean CheckUser(ClientLogInData user);
    public boolean RemoveUser(ClientLogInData user);
}
