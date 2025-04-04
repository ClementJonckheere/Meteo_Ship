package com.meteo.service;

import com.meteo.db.OracleConnection;
import com.meteo.model.Favori;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.meteo.model.FavoriWidget;


public class FavorisService {

    // Ajouter un favori 
    public static void ajouterFavori(int userId, String city, String country) {
        System.out.println("Ajout d'un favori : " + city);

        String checkSql = "SELECT COUNT(*) FROM favoris WHERE user_id = ? AND city = ?";
        String insertSql = "INSERT INTO favoris (id, user_id, city, country) VALUES (FAVORIS_SEQ.NEXTVAL, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            checkStmt.setString(2, city);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Ce favori existe déjà");
                return;
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setString(2, city);
                insertStmt.setString(3, country);
                insertStmt.executeUpdate();
                System.out.println("Favori ajouté avec succès");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de l'ajout du favori : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Récupérer les favoris 
    public static List<Favori> getFavoris(int userId) {
        List<Favori> favoris = new ArrayList<>();
        String sql = "SELECT id, city, country FROM favoris WHERE user_id = ?";

        System.out.println("Récupération des favoris pour userId: " + userId);

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Favori favori = new Favori(rs.getInt("id"), userId, rs.getString("city"), rs.getString("country"));
                favoris.add(favori);
                System.out.println("Favori récupéré: " + favori.getCity() + ", " + favori.getCountry());
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération des favoris : " + e.getMessage());
            e.printStackTrace();
        }

        return favoris;
    }

    // Supprimer un favori
    public static void supprimerFavori(int userId, String city) {
        System.out.println("Suppression du favori : " + city);

        String deleteSql = "DELETE FROM favoris WHERE user_id = ? AND city = ?";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            deleteStmt.setInt(1, userId);
            deleteStmt.setString(2, city);
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Favori supprimé !");
            } else {
                System.out.println("Aucun favori supprimé. La ville n'existe peut-être pas.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la suppression du favori : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static FavoriWidget getWidgetPreferences(int userId, String city) {
        String sql = "SELECT id, show_temp, show_humidity, show_wind FROM favori_widgets WHERE user_id = ? AND city = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, city);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new FavoriWidget(
                    rs.getInt("id"),
                    userId,
                    city,
                    rs.getInt("show_temp") == 1,
                    rs.getInt("show_humidity") == 1,
                    rs.getInt("show_wind") == 1
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new FavoriWidget(0, userId, city, true, true, true); 
    }
    
    public static void updateWidgetPreferences(int userId, String city, boolean showTemp, boolean showHumidity, boolean showWind) {
        String sql = "MERGE INTO favori_widgets USING dual ON (user_id = ? AND city = ?) " +
                     "WHEN MATCHED THEN UPDATE SET show_temp = ?, show_humidity = ?, show_wind = ? " +
                     "WHEN NOT MATCHED THEN INSERT (user_id, city, show_temp, show_humidity, show_wind) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, city);
            stmt.setInt(3, showTemp ? 1 : 0);
            stmt.setInt(4, showHumidity ? 1 : 0);
            stmt.setInt(5, showWind ? 1 : 0);
            stmt.setInt(6, userId);
            stmt.setString(7, city);
            stmt.setInt(8, showTemp ? 1 : 0);
            stmt.setInt(9, showHumidity ? 1 : 0);
            stmt.setInt(10, showWind ? 1 : 0);

            stmt.executeUpdate();
            System.out.println("Préférences mises à jour pour " + city);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
