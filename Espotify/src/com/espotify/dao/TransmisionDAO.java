package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Transmision;
import com.mysql.cj.jdbc.Blob;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TransmisionDAO {
			
	private final static String INSERT_ESTACION_QUERY = "INSERT INTO Reproductor_musica.Estacion (url, libre) VALUES (?,?)";
	private final static String INSERT_TRANSM_QUERY = "INSERT INTO Reproductor_musica.TransmisionVivo (nombre, descripcion, activa, usuario, estacion) VALUES (?,?,?,?,?)";
	private final static String GET_ESTACION_QUERY = "SELECT id FROM Reproductor_musica.Estacion WHERE libre = 1 AND url = ? LIMIT 1";
	private final static String GET_URLESTACIONLIBRE_QUERY = "SELECT DISTINCT url FROM Reproductor_musica.Estacion WHERE libre = 1";
	private final static String GET_TRANSM_QUERY = "SELECT id FROM Reproductor_musica.TransmisionVivo WHERE estacion = ? ORDER BY id DESC LIMIT 1";
	private final static String GET_IDESTACION_QUERY = "SELECT estacion FROM Reproductor_musica.TransmisionVivo WHERE id = ?";
			
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET descripcion=? WHERE id = ?";
	private final static String UPDATE_ESTACION_QUERY = "UPDATE Reproductor_musica.Estacion SET libre=? WHERE id = ?";
	private final static String UPDATE_TRANSM_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET activa=? WHERE id = ?";
	private final static String UPDATE_TRANSM2_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET estacion=? WHERE id = ?";
	
	private final static String DELETE_ESTACION_QUERY =	"DELETE FROM Reproductor_musica.Estacion WHERE url = ?";
	private final static String DELETE_TRANSM_QUERY =	"DELETE FROM Reproductor_musica.TransmisionVivo WHERE id = ?";
	
	private final static String GET_TRANSM_NOMBRE_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.activa activa, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.nombre = ?";
	private final static String GET_TRANSM_ALL_QUERY = "SELECT * FROM Reproductor_musica.TransmisionVivo transmision"
			+ "WHERE transmision.estacion = estacion.id AND transmision.nombre = ?";

	
	private final static String GET_TRANSM_ID_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.activa activa, transmision.usuario usuario, estacion.url url " 
			+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
			+ "WHERE transmision.estacion = estacion.id AND transmision.id = ?";
	
	private final static String GET_TRANSM_USERS_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.activa activa, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.usuario = ?";
	private final static String GET_USERS_SEGUIDOS_QUERY = "SELECT usuario2 FROM Reproductor_musica.Sigue WHERE usuario1 = ?";
	private final static String GET_ID_TRANSMISION_QUERY = "SELECT id FROM Reproductor_musica.TransmisionVivo WHERE nombre = ?";

	
	// -------------------------------------------------------------------------------
	
	/*
	 * Parametros: URL de un punto de montaje en Icecast
	 * Devuelve: false en caso de error, true si se ha añadido la estación con la URL
	*/
	public static boolean anyadirEstacion(String URL) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_ESTACION_QUERY);
			
			ps.setString(1, URL);
			ps.setBoolean(2, true);

			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	/*
	 * Parametros: URL de un punto de montaje en Icecast
	 * Devuelve: false en caso de error, true si se ha borrado la estación con la URL
	*/
	public static boolean borrarEstacion(String url) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_ESTACION_QUERY);
			ps.setString(1, url);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	// -------------------------------------------------------------------------------

	/*
	 * Parametros: Ninguno
	 * Devuelve: Una lista con las URL de todas las estaciones libres 
	*/
	public static List<String> getURLestacionesLibres() {
		List<String> estacionesURL = new ArrayList<String>();
		try {
			
			Connection conn = ConnectionManager.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(GET_URLESTACIONLIBRE_QUERY);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				estacionesURL.add(rs.getString("url"));
			}
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		return estacionesURL;
	}
	
	/*
	 * Parametros: nombre de la transmision, descripción, id del usuario que la inicia
	 * Devuelve: null en caso de error, un dato de tipo Transmision con el id de la misma, 
	 * 			 el nombre, la descipcion, un boleano para indicar que esta activa, 
	 * 			 el id del usuario y la URL de la estación asociada.
	*/
	public static Transmision iniciar(String nombre, String descripcion, int usuario, String URL) {
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(GET_ESTACION_QUERY);
			ps.setString(1, URL);
			ResultSet rs = ps.executeQuery();
			int idTransmision = 0;
			int idEstacion = 0; 
			while(rs.next()) {
				idEstacion = rs.getInt("id");
			}
			
			if (idEstacion == 0) {
				return null;
			}
			else {
				System.out.println("PASADO GET");
				ps = conn.prepareStatement(UPDATE_ESTACION_QUERY);
				ps.setInt(1, 0);
				ps.setInt(2, idEstacion);
				ps.executeUpdate();
				System.out.println("PASADO UPDATE");
				ps = conn.prepareStatement(INSERT_TRANSM_QUERY);
				ps.setString(1, nombre);
				ps.setString(2, descripcion);
				ps.setInt(3, 1);
				ps.setInt(4, usuario);
				ps.setInt(5, idEstacion);
				ps.executeUpdate();
							
				ps = conn.prepareStatement(GET_TRANSM_QUERY);
				ps.setInt(1, idEstacion);
				rs = ps.executeQuery(); 
				while(rs.next()) idTransmision = rs.getInt("id");
			}
						
			ConnectionManager.releaseConnection(conn);
			System.out.println("NO PROBLEMA SQL");
			Transmision transmision = new Transmision(idTransmision, nombre, descripcion, true, usuario, URL);
			return transmision;
			
		} catch(SQLException se) {
			System.out.println("PROBLEMA SQL");
			System.out.println(se.getMessage());
			return null;
		} catch(Exception e) {
			System.out.println("EXCEPTION");
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	/*
	 * Parametros: id de la transmision utilizada, URL de la estacion asociada 
	 * Devuelve: false en caso de error, true si se ha detenido la transmision correctamente (no la borra)
	*/
	public static boolean parar(int idTransmision, String url) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(GET_IDESTACION_QUERY);
			ps.setInt(1, idTransmision);
			ResultSet rs = ps.executeQuery();
			int idEstacion = 0;
			while(rs.next()) idEstacion = rs.getInt("estacion");
			
			ps = conn.prepareStatement(UPDATE_TRANSM2_QUERY);
			ps.setString(1, null);
			ps.setInt(2, idTransmision);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(UPDATE_TRANSM_QUERY);
			ps.setInt(1, 0);
			ps.setInt(2, idTransmision);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(UPDATE_ESTACION_QUERY);
			ps.setInt(1, 1);
			ps.setInt(2, idEstacion);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	/*
	 * Par�metros: id del usuario
	 * Devuelve: id de la transmisi�n
	 */
	public static int getIdTransmision(String nombre) {
		int id = -1;
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_ID_TRANSMISION_QUERY);
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				id = rs.getInt(1);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return id;
	}
	
	/*
	 * Parametros: id de la transmision utilizada, URL de la estacion asociada 
	 * Devuelve: false en caso de error, true si se ha eliminado la transmision correctamente
	*/
	public static boolean finalizar(int idTransmision, String url) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			
			PreparedStatement ps = conn.prepareStatement(GET_IDESTACION_QUERY);
			ps.setInt(1, idTransmision);
			ResultSet rs = ps.executeQuery();
			int idEstacion = 0;
			while(rs.next()) idEstacion = rs.getInt("estacion");
			
			ps = conn.prepareStatement(DELETE_TRANSM_QUERY);
			ps.setInt(1, idTransmision);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(UPDATE_ESTACION_QUERY);
			ps.setInt(1, 1);
			ps.setInt(2, idEstacion);
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	/*
	 * Parametros: nombre nuevo, descripcion nueva, id de la transmision a cambiar
	 * Devuelve: false en caso de error o no cambio, true si se ha cambiado o nombre o descripcion
	*/
	public static boolean cambiar_info(String nombre, String descripcion, int id) {
	
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;
			boolean cambiada = false;

			if(nombre != null && !nombre.equals("")) {
				ps = conn.prepareStatement(UPDATE_NOM_QUERY);
				
				ps.setString(1, nombre);
				ps.setInt(2, id);
				ps.executeUpdate();
				cambiada = true;
			}
			if(descripcion != null) {
				ps = conn.prepareStatement(UPDATE_DES_QUERY);
				
				ps.setString(1, descripcion);
                ps.setInt(2, id);
				ps.executeUpdate();
				cambiada = true;
			}
			
			ConnectionManager.releaseConnection(conn);
			return cambiada;
			
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	/*
	 * Parametros: nombre de una transmision
	 * Devuelve: una lista de datos de tipo Transmision 
	 * 			 (id de la misma, nombre, descipcion, boleano que indica si esta activa, 
	 * 			 id del usuario, URL de la estación asociada)
	*/
	public static List<Transmision> getTransmisionPorNombre(String nombre) {
		List<Transmision> directos = new ArrayList<Transmision>();
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_TRANSM_NOMBRE_QUERY);
            
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Transmision result = new Transmision(rs.getInt("id"), rs.getString("nombre"), 
									rs.getString("descripcion"), rs.getBoolean("activa"), 
									rs.getInt("usuario"), rs.getString("url"));
                directos.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return directos;
	}
	
	/*
	 * Parametros: 
	 * Devuelve: una lista de datos de tipo Transmision 
	 * 			 (id de la misma, nombre, descipcion, boleano que indica si esta activa, 
	 * 			 id del usuario, URL de la estación asociada)
	*/
	public static List<Transmision> getTodasTransmisiones() {
		List<Transmision> directos = new ArrayList<Transmision>();
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_TRANSM_ALL_QUERY);
            
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Transmision result = new Transmision(rs.getInt("id"), rs.getString("nombre"), 
									rs.getString("descripcion"), rs.getBoolean("activa"), 
									rs.getInt("usuario"), rs.getString("url"));
                directos.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return directos;
	}
	
	
	public static Transmision getTransmisionPorId(int id) {
		Transmision result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_TRANSM_ID_QUERY);
            
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				result = new Transmision(rs.getInt("id"), rs.getString("nombre"), 
									rs.getString("descripcion"), rs.getBoolean("activa"), 
									rs.getInt("usuario"), rs.getString("url"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	/*
	 * Parametros: id de un usuario
	 * Comentarios: Obtiene las transmisiones activas de los usuarios a los que sigue "usuario"
	 * Devuelve: una lista de datos de tipo Transmision 
	 * 			 (id de la misma, nombre, descipcion, boleano que indica si esta activa, 
	 * 			 id del usuario, URL de la estación asociada)
	*/
	public static List<Transmision> getTransmisionesUsersSeguidos(int usuario) {
		List<Transmision> directos = new ArrayList<Transmision>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_USERS_SEGUIDOS_QUERY);
            
			ps.setInt(1, usuario);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				PreparedStatement ps2 = conn.prepareStatement(GET_TRANSM_USERS_QUERY);
				ps2.setInt(1, rs.getInt("usuario2"));
				ResultSet rs2 = ps2.executeQuery();
				
				while(rs2.next()){
					Transmision result = new Transmision(rs2.getInt("id"), rs2.getString("nombre"), 
										rs2.getString("descripcion"), rs2.getBoolean("activa"), 
										rs2.getInt("usuario"), rs2.getString("url"));
	                directos.add(result);
				}
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return directos;
	}
}

