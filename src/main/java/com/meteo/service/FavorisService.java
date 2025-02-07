package com.meteo.service;

import com.meteo.db.OracleConnection;
import com.meteo.model.Favori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavorisService {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:FREEPDB1";
    private static final String DB_USER = "admeteo";
    private static final String DB_PASSWORD = "azerty";
    
   

    public static void ajouterFavori(int userId, String city, String country) {
        System.out.println("📌 Vérification si le favori existe déjà...");

        String checkSql = "SELECT COUNT(*) FROM favoris WHERE user_id = ? AND city = ?";
        String insertSql = "INSERT INTO favoris (id, user_id, city, country) VALUES (FAVORIS_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            checkStmt.setString(2, city);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("⚠️ Ce favori existe déjà !");
                return; // Ne pas insérer si la ville est déjà en favori
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, city);
                insertStmt.setString(3, country);
                insertStmt.executeUpdate();
                System.out.println("✅ Favori ajouté avec succès !");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            e.printStackTrace();
        }
    }






    public static List<Favori> getFavoris(int userId) {
        List<Favori> favoris = new ArrayList<>();
        String sql = "SELECT id, city, country FROM favoris WHERE user_id = ?";
        
        System.out.println("📌 Récupération des favoris pour userId: " + userId);

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Favori favori = new Favori(rs.getInt("id"), userId, rs.getString("city"), rs.getString("country"));
                favoris.add(favori);
                System.out.println("✅ Favori récupéré: " + favori.getCity() + ", " + favori.getCountry());
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lors de la récupération des favoris : " + e.getMessage());
            e.printStackTrace();
        }

        return favoris;
    }

}
