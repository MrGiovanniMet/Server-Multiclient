package com.mycompany.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.DataOutputStream;
import com.google.gson.Gson;

class Recibe extends Thread {

    final DataInputStream dis;  //leer los datos
    final DataOutputStream dos; // para mandar
    final Socket socket;
    boolean registrado = false;
    Gson gson = new Gson();

    public Recibe(Socket socket, DataInputStream dis, DataOutputStream dos) {
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
    }

    public void run() {
        String received;

        try {

            while (true) {
                registrado = false;
                // aqui leemos el JSON lo cambiamos a la clase usuario
                received = dis.readUTF();
                System.out.println("Mensaje recibido: " + received);
                User u = gson.fromJson(received, User.class);
                // sacamos el nombre  para guardarlo en cliente
                String name = u.name;

                // aqui lo que hago es por asi decir cuando la bd se conecte habra un id supongo
                // y ese id servira para q no se repita pero aqui uso el nombre para que no haya
                // 2 persoans con el mismo nombre no sabria a quien enviar en este caso
                // la pk es el nombre
                for (Cliente c : Server.clientes) {
                    if (c.nombre.equals(name)) {
                        registrado = true;
                        break;
                    }
                }

                if (!registrado) {
                    // si es falso se registra de lo contrario nop
                    Server.clientes.add(new Cliente(name, dos));
                }
                /*
                // recorremos otra vez para mandar el broadcast
                for (Cliente c : Server.clientes) {
                    if (!(c.nombre.equals(name))) {
                        c.dos.writeUTF(received);
                        c.dos.flush();

                    }
                }
                 */
                try {
                    Server.cola.put(new Mensaje(name, received));
                } catch (InterruptedException e) {
                    System.out.println("Error");
                }
            }

        } catch (IOException e) {
            System.out.println("Error al recibir mensaje: " + e);
        }
        try {
            dis.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar los recursos: " + e);
        }
    }
}
