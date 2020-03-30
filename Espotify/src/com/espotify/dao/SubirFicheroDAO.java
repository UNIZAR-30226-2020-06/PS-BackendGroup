package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.mysql.cj.xdevapi.Statement;

public class SubirFicheroDAO {
	
	public static final String GET_LAST_SONG_ID_QUERY = "SELECT id FROM Reproductor_musica.Audio ORDER BY ID DESC LIMIT 1";
	
	public static int obtenerUltimaCancionId() {
		int id = 0;
		
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_LAST_SONG_ID_QUERY);
			ResultSet rs = ps.executeQuery();
		  
			while (rs.next()) {
			    id = rs.getInt("id");
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}

}
