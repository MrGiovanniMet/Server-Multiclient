/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author claude
 */
public class MySQLHandler 
{
    private static final String URL = "jdbc:mysql://localhost:3306/chatmultiusuario";
    public static final String USER = "root";
    public static final String PSWD ="Win2002Racedb$";
    
    public static Connection getConnection()
    {
       Connection connection = null;
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           connection = (Connection)DriverManager.getConnection(URL,USER,PSWD);
       }catch(Exception e){
           System.out.println("System error:\n"+ e.getMessage());
       }
       return connection;
   }
}
