/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;
import java.io.DataOutputStream;

/**
 *
 * @author gio
 */
public class Cliente {
    String nombre;
    DataOutputStream dos;
    
    public Cliente(String nombre, DataOutputStream dos) {
    this.nombre = nombre;
    this.dos = dos;
}

}
