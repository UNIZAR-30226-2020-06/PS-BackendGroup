package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;


public class CancionDAO {
	private static String nombreGenero = null;
	private static String nombreAutor = null;
	
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Audio (titulo, usuario, genero, url) VALUES (?,?,?, 'url')";
	private final static String GET_ID_GENERO_QUERY = "SELECT g.id FROM Reproductor_musica.Genero g WHERE nombre = ?";
	private final static String GET_ID_AUTOR_QUERY = "SELECT a.id FROM Reproductor_musica.Usuario a WHERE nombre = ?";
	private final static String INSERT_URL_QUERY = "UPDATE Reproductor_musica.Audio SET url = ? WHERE id = ?";
	private final static String DELETE_QUERY = "DELETE FROM Reproductor_musica.Audio WHERE id = ?";
	private final static String GET_ID_CANCION_QUERY = "SELECT a.id FROM Reproductor_musica.Audio a WHERE titulo = ?";
	private final static String GET_ID_ULTIMA_CANCION_QUERY = "SELECT a.id FROM Reproductor_musica.Audio a ORDER BY a.id DESC LIMIT 1";

	
	public int subirCancion(String titulo, String autor, String genero) {
		System.out.println("SubirCancion Entro+++++++++++++++++");
		int id_autor = obtenerIDAutor(autor);
		int id_genero = obtenerIDGenero(genero);
		System.out.println(id_genero);
		if (insertarCancion(titulo, id_autor, id_genero)) {
			return 1;
		}
		return 0;
	}
	
	public boolean insertar_url(String url) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_URL_QUERY);
			int id = obtenerUltimaCancion();
			System.out.println(id);
			ps.setString(1, url);
			ps.setInt(2, id);
			
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SE HA INSERTADO LA URL");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al insertar url");
			return false;
		}	
	}
	
	
	public boolean borrarCancion(String titulo, int id) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			if (id == 0)
				id = obtenerIdCancion(titulo);
			ps.setInt(1, id);
			
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SE HA BORRADO LA CANCION");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al borrar cancion");
			return false;
		}	
	}
	
	public boolean modificarCancion(String titulo, String genero){
		Connection conn;
		int id_genero = obtenerIDGenero(genero);
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setString(1, titulo);
			ps.setInt(2, id_genero);
			ps.setInt(3, 3);
			
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SE HA MODIFICA LA CANCION");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al modificar cancion");
			return false;
		}	
	}
	
	
	private int obtenerIDGenero(String genero) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_GENERO_QUERY);
			
			ps.setString(1, genero);
			
			ResultSet rs = ps.executeQuery();
			int id_autor= 0;
			
			while(rs.next()) {
				System.out.println("Entro----------------------------------");
				id_autor = rs.getInt(1);
			}
			ConnectionManager.releaseConnection(conn);
			
			return id_autor;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del genero de la canción");
			return 0;
		}
	}


	private int obtenerIDAutor(String autor) {
		System.out.println("obtenerIDAUTOR Entro +++++++++++++++++");
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_AUTOR_QUERY);
			
			ps.setString(1, autor);
			
			ResultSet rs = ps.executeQuery();
			System.out.println("EXITOO");
			int id_autor = 0;
			while(rs.next())
				id_autor = rs.getInt(1);
			ConnectionManager.releaseConnection(conn);
			
			return id_autor;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del autor de la canción");
			return 0;
		}
	}


	private boolean insertarCancion(String titulo, int id_autor, int id_genero) {
		System.out.println("insertarCancion Entro +++++++++++++++++");
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			System.out.println(titulo);
			System.out.println(id_autor);
			System.out.println(id_genero);
			ps.setString(1, titulo);
			ps.setInt(2, id_autor);
			ps.setInt(3, id_genero);
			
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("insertarCancion Salgo +++++++++++++++++");			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al insertar la canción");
			return false;
		}
	}

	private int obtenerIdCancion(String titulo) {
		System.out.println("obtenerIDCancion Entro +++++++++++++++++");
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_CANCION_QUERY);
			
			ps.setString(1, titulo);
			
			ResultSet rs = ps.executeQuery();
			int id = 0;
			while(rs.next())
				id = rs.getInt(1);
			ConnectionManager.releaseConnection(conn);
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id de la ultima canción");
			return 0;
		}
	}
		
		private int obtenerUltimaCancion() {
			System.out.println("obtenerIDCancion Entro +++++++++++++++++");
			Connection conn;
			try {
				conn = ConnectionManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(GET_ID_ULTIMA_CANCION_QUERY);				
				ResultSet rs = ps.executeQuery();
				int id = 0;
				while(rs.next())
					id = rs.getInt(1);
				ConnectionManager.releaseConnection(conn);
				return id;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error al obtener el id de la canción");
				return 0;
			}
	}
}
