package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Genero;

public class GeneroDAO {
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Genero (nombre,tipo) VALUES (?,?)";
	private final static String DELETE_QUERY = "DELETE FROM Reproductor_musica.Genero WHERE id = ?";
	private final static String UPDATE_NOMBRE_QUERY = "UPDATE Reproductor_musica.Genero SET nombre = ? WHERE id = ?";
	private final static String GET_GENERO_MUSICA = "SELECT g.id, g.nombre FROM Reproductor_musica.Genero g WHERE g.tipo = 'cancion'";
	private final static String GET_NOMBRE_GENERO_MUSICA = "SELECT g.nombre FROM Reproductor_musica.Genero g WHERE g.id = ? AND g.tipo = 'cancion'";

	private final static String GET_ID_GENERO_PODCAST = "SELECT g.id FROM Reproductor_musica.Genero g WHERE g.tipo = 'podcast'";
	private final static String GET_NOMBRE_GENERO_PODCAST = "SELECT g.nombre FROM Reproductor_musica.Genero g WHERE g.id = ? AND g.tipo = 'podcast'";
	private final static String GET_ID_GENERO = "SELECT g.id FROM Reproductor_musica.Genero g WHERE g.nombre = ?";
	
	private final static String CANCION = "cancion";
	private final static String PODCAST = "podacast";
	private final static String GET_GENERO_CAPITULO = "SELECT g.id, g.nombre FROM Reproductor_musica.Genero g WHERE g.tipo = 'capituloPodcast'";
	private final static String CAPITULO = "capituloPodcast";

	// Devuelve True si se ha añadido un nuevo genero correctamente
	public static boolean anyadir(String nombre, String tipo) {
		try {
			// Comprobar tipo de genero valido
			if (!tipo.equals("cancion") && !tipo.equals("podcast") && !tipo.equals("capituloPodcast")) {
				return false;
			}
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			ps.setString(1, nombre);
			ps.setString(2, tipo);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return false;
	}
	
	// Devuelve True si se ha eliminado el genero correctamente
	public static boolean eliminar(int idGenero) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			ps.setInt(1, idGenero);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return false;
	}
	
	// Devuelve True si se ha eliminado el genero correctamente
	public static boolean cambiarNombre(int idGenero, String newName) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_NOMBRE_QUERY);
			ps.setString(1, newName);
			ps.setInt(1, idGenero);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return false;
	}
	
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
	

	public int obtenerIdGenero(String genero) {
		int idGenero = -1;
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_GENERO);
			ps.setString(1, genero);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				idGenero = rs.getInt(1);
			}
			ConnectionManager.releaseConnection(conn);
			
			return idGenero; 
		} catch (SQLException e) {
			System.out.println("Error al obtener el id del generos");
			return -2;
		}	
	}
		

	public ArrayList<Genero> obtenerGeneroCapitulo() {
		ArrayList<Genero> generos = new ArrayList<Genero>();
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_GENERO_CAPITULO);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				generos.add(new Genero(rs.getInt(1), rs.getString(2), CAPITULO));
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
			System.out.println("Error al obtener el id de la canci�n");
			return null;
		}
	}
}
