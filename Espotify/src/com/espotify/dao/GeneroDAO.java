package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Genero;

public class GeneroDAO {
	private final static String GET_GENERO_MUSICA = "SELECT g.id, g.nombre FROM Reproductor_musica.Genero g WHERE g.tipo = 'cancion'";
	private final static String GET_NOMBRE_GENERO_MUSICA = "SELECT g.nombre FROM Reproductor_musica.Genero g WHERE g.id = ? AND g.tipo = 'cancion'";
	private final static String GET_ID_GENERO_PODCAST = "SELECT g.id FROM Reproductor_musica.Genero g WHERE g.tipo = 'podcast'";
	private final static String GET_NOMBRE_GENERO_PODCAST = "SELECT g.nombre FROM Reproductor_musica.Genero g WHERE g.id = ? AND g.tipo = 'podcast'";

	private final static String CANCION = "cancion";
	private final static String PODCAST = "podacast";

	
	public ArrayList<Genero> obtenerGeneroMusica() {
		ArrayList<Genero> generos = new ArrayList<Genero>();
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_GENERO_MUSICA);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				generos.add(new Genero(rs.getInt(1), rs.getString(2), CANCION));
			ConnectionManager.releaseConnection(conn);
			return generos; 
		} catch (SQLException e) {
			System.out.println("Error al obtener los ids de los generos");
			return null;
		}	
	}

	
	private String obtenerNombreMusica(int id) {
		System.out.println("obtenerIDCancion Entro +++++++++++++++++");
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_NOMBRE_GENERO_MUSICA);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			String nombre = null;
			while(rs.next())
				nombre = rs.getString(1);
			ConnectionManager.releaseConnection(conn);
			return nombre;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id de la canción");
			return null;
		}
	}
}