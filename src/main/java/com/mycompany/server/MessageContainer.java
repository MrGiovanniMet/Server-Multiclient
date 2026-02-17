/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.server;

import com.mycompany.server.Json.Message;

/**
 *
 * @author claude
 */
public record MessageContainer(ClientHandler from, Message message){
    
}
