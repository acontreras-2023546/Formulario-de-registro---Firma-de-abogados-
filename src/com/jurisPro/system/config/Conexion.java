/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jurisPro.system.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author informatica
 */


public class Conexion {

    public static Connection getConnection() throws SQLException {
        try {
            // Aseguramos la carga del driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL JDBC no encontrado en las librerías.");
        }

        return DriverManager.getConnection(
            Enviroment.getUrl(), 
            Enviroment.USER, 
            Enviroment.PASSWORD
        );
    }
}
