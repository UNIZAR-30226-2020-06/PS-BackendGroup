package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;


public class CapituloPodcastDAO {
	private static String nombreGenero = null;
	private static String nombreAutor = null;
	
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Audio (titulo, usuario, genero, url) VALUES (?,?,?, ?)";
	private final static String GET_ID_GENERO_QUERY = "SELECT g.id FROM Reproductor_musica.Genero g WHERE g.nombre = ? AND g.tipo = 'CapituloPodcast'";
	private final static String GET_ID_AUTOR_QUERY = "SELECT a.id FROM Reproductor_musica.Usuario a WHERE nombre = ?";
	private final static String INSERT_URL_QUERY = "UPDATE Reproductor_musica.Audio SET url = ? WHERE id = ?";
	private final static String DELETE_QUERY = "DELETE FROM Reproductor_musica.Audio WHERE id = ?";
	private final static String GET_ID_CAPITULO_PODCAST_QUERY = "SELECT a.id FROM Reproductor_musica.Audio a WHERE titulo = ?";
	private final static String GET_ID_ULTIMO_CAPITULO_PODCAST_QUERY = "SELECT a.id FROM Reproductor_musica.Audio a ORDER BY a.id DESC LIMIT 1";
	private final static String UPDATE_QUERY = "UPDATE Reproductor_musica.Audio SET titulo = ?, genero = ? WHERE id = ?";
	private final static String GET_NOMBRE_AUTOR_QUERY = "SELECT a.nombre FROM Reproductor_musica.Usuario a WHERE a.id = ?";
	private final static String GET_NOMBRE_GENERO_QUERY = "SELECT g.nombre FROM Reproductor_musica.Genero g WHERE g.id = ? AND g.tipo = 'CapituloPodcast'";

	
	public int subirCapituloPodcast(String titulo, String autor, String genero, String ruta) {
		int id_autor = obtenerIDAutor(autor);
		int id_genero = obtenerIDGenero(genero);
		System.out.println(id_genero);
		if (insertarCapituloPodcast(titulo, id_autor, id_genero, ruta)) {
			return 1;
		}
		return 0;
	}
	
	public boolean insertar_url(String url) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_URL_QUERY);
			int id = obtenerUltimoCapituloPodcast();
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
	
	
	public boolean borrarCapituloPodcast(int id) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			ps.setInt(1, id);
			
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SE HA BORRADO EL CAPITULO PODCAST");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al borrar Capitulo Podcast");
			return false;
		}	
	}
	
	public boolean modificarCapituloPodcast(String titulo, String genero, int id){
		Connection conn;
		int id_genero = obtenerIDGenero(genero);
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_QUERY);
			ps.setString(1, titulo);
			ps.setInt(2, id_genero);
			ps.setInt(3, id);
			System.out.println(titulo);
			System.out.println(id);
			System.out.println(id_genero);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SE HA MODIFICA EL CAPITULO PODCAST");
			return true;
		} catch (SQLException e) {
			System.out.println("Error al modificar Capitulo Podcast");
			return false;
		}	
	}
	
	
	public String obtenerNombreAutor(int id_autor) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_NOMBRE_AUTOR_QUERY);
			ps.setInt(1, id_autor);
			ResultSet rs = ps.executeQuery();
			String nombre = null;
			while(rs.next())
				nombre = rs.getString(1);
			ConnectionManager.releaseConnection(conn);
			return nombre;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del Capitulo Podcast");
			return null;
		}
	}
	
	public String obtenerNombreGenero(int id_genero) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_NOMBRE_GENERO_QUERY);
			ps.setInt(1, id_genero);
			ResultSet rs = ps.executeQuery();
			String nombre = null;
			while(rs.next())
				nombre = rs.getString(1);
			ConnectionManager.releaseConnection(conn);
			return nombre;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del Capitulo Podcast");
			return null;
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
				id_autor = rs.getInt(1);
			}
			ConnectionManager.releaseConnection(conn);
			
			return id_autor;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del genero del Capitulo Podcast");
			return 0;
		}
	}


	private int obtenerIDAutor(String autor) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_AUTOR_QUERY);
			
			ps.setString(1, autor);
			
			ResultSet rs = ps.executeQuery();
			int id_autor = 0;
			while(rs.next())
				id_autor = rs.getInt(1);
			ConnectionManager.releaseConnection(conn);
			
			return id_autor;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del autor del Capitulo Podcast");
			return 0;
		}
	}


	private boolean insertarCapituloPodcast(String titulo, int id_autor, int id_genero, String ruta) {
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
			ps.setString(4, ruta);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al insertar el Capitulo Podcast");
			return false;
		}
	}

	private int obtenerIdCapituloPodcast(String titulo) {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_CAPITULO_PODCAST_QUERY);
			
			ps.setString(1, titulo);
			
			ResultSet rs = ps.executeQuery();
			int id = 0;
			while(rs.next())
				id = rs.getInt(1);
			ConnectionManager.releaseConnection(conn);
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del ultimo Capitulo Podcast");
			return 0;
		}
	}
		
	private int obtenerUltimoCapituloPodcast() {
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_ULTIMO_CAPITULO_PODCAST_QUERY);				
			ResultSet rs = ps.executeQuery();
			int id = 0;
			while(rs.next())
				id = rs.getInt(1);
			ConnectionManager.releaseConnection(conn);
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error al obtener el id del Capitulo Podcast");
			return 0;
		}
	}
}
