package com.meteo.dao;

import com.meteo.db.OracleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.bcrypt.BCrypt;

public class UserDAO {
	
	// Récupérer l'ID de l'utilisateur avec son nom d'utilisateur ou email.
	public int getUserId(String login) {
	    String query = "SELECT id FROM users WHERE username = ? OR email = ?";
	    try (Connection conn = OracleConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, login);
	        stmt.setString(2, login);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("id");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}

	//  Vérifier si l'utilisateur existe et si son mot de passe est correct
	public boolean validateUser(String login, String password) {
	    String query = "SELECT password FROM users WHERE username = ? OR email = ?";
	    
	    try (Connection conn = OracleConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, login);
	        stmt.setString(2, login);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            String storedHashedPassword = rs.getString("password");
	            return BCrypt.checkpw(password, storedHashedPassword); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	// Inscrire un nouvel utilisateur dans la base de données
	public boolean registerUser(String username, String email, String password) {
	    String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
	    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

	    try (Connection conn = OracleConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, username);
	        stmt.setString(2, email);
	        stmt.setString(3, hashedPassword);
	        int rowsInserted = stmt.executeUpdate();
	        return rowsInserted > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
