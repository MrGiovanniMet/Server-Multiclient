/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

/**
 *
 * @author gio
 */
class Broadcast extends Thread {

    public void run() {
        try {
            while (true) {
                Mensaje m = Server.cola.take(); // espera sin bloquear CPU

                for (Cliente c : Server.clientes) {
                    if (!c.nombre.equals(m.from)) {
                        c.dos.writeUTF(m.json);
                        c.dos.flush();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en broadcast: " + e);
        }
    }
}