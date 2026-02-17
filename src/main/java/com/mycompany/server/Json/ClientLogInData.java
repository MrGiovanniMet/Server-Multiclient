/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.mycompany.server.Json;

/**
 *
 * @author claude
 */
public record ClientLogInData(String userName,String password) {
    public ClientLogInData(){
        this("","");
    }
}
