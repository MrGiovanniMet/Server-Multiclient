/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.server;

import com.mycompany.server.Json.AuthResponse;
import com.mycompany.server.Json.ClientLogInData;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author claude
 */
public class AuthService 
{
    private final DataSource dataSource;
    private final String QUERY = "SELECT user, pass FROM usuario WHERE user = ? AND pass = ?";
    public AuthService(DataSource ds)
    {
        this.dataSource=ds;
    }
    public AuthResponse ValidateUser(ClientLogInData user) throws SQLException
    {
        try(Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(QUERY))
        {
            ps.setString(1, user.userName());
            ps.setString(2, user.password());
            ResultSet rs = ps.executeQuery();
            
            if(rs.next())
                return new AuthResponse(true,"Inicio de sesion exitoso");
            return new AuthResponse(false,"usuario o contrasena incorrecta");
        }
    }
}
