/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
/**
 *
 * @author claude
 */
public class MySQLHandler 
{
    private static HikariDataSource dataSource=null;
    static
    {
        HikariConfig config= new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/chatmultiusuario");
        config.setUsername("root");
        config.setPassword("Win2002Racedb$");
        
        dataSource = new HikariDataSource(config);
    }
    
    public static DataSource getConnection()
    {
       return dataSource;
    }
}
