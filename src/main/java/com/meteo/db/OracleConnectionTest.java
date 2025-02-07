package com.meteo.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        String user = "system";
        String password = "azerty";

        try {
            Class.forName("oracle.jdbc.OracleDriver");

            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion réussie à Oracle !");
            
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver JDBC non trouvé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL !");
            e.printStackTrace();
        }
    }
}
