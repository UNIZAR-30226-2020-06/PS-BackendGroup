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
	private final static String INSERT_TRANSM_QUERY = "INSERT INTO Reproductor_musica.TransmisionVivo (nombre, descripcion, usuario, estacion) VALUES (?,?,?,?)";
	private final static String GET_ESTACION_QUERY = "SELECT id, url FROM Reproductor_musica.Estacion WHERE libre = true ORDER BY id DESC LIMIT 1";
	private final static String GET_TRANSM_QUERY = "SELECT id FROM Reproductor_musica.TransmisionVivo ORDER BY id DESC LIMIT 1";
			
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET descripcion=? WHERE id = ?";
	private final static String UPDATE_ESTACION_QUERY = "UPDATE Reproductor_musica.Estacion SET libre=? WHERE url = ?";
	
	private final static String DELETE_ESTACION_QUERY =	"DELETE FROM Reproductor_musica.Estacion WHERE url = ?";
	private final static String DELETE_TRANSM_QUERY =	"DELETE FROM Reproductor_musica.TransmisionVivo WHERE id = ?";
	
	private final static String GET_TRANSM_NOMBRE_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.nombre = ?";
	private final static String GET_TRANSM_USERS_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.usuario = ?";
	private final static String GET_USERS_SEGUIDOS_QUERY = "SELECT usuario2 FROM Reproductor_musica.Sigue WHERE usuario1 = ?";
	
	
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
	 * Parametros: nombre de la transmision, descripción, id del usuario que la inicia
	 * Devuelve: null en caso de error, un dato de tipo Transmision con el id de la misma, 
	 * 			 el nombre, la descipcion, el id del usuario y la URL de la estación asociada.
	*/
	public static Transmision iniciar(String nombre, String descripcion, int usuario) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(GET_ESTACION_QUERY);
			ResultSet rs = ps.executeQuery();
			int idTransmision = 0;
			int idEstacion = 0; 
			String URL = null;
			while(rs.next()) {
				idEstacion = rs.getInt("id");
				URL = rs.getString("url");
			}
			
			if (URL == null) {
				return null;
			}
			else {
				ps = conn.prepareStatement(UPDATE_ESTACION_QUERY);
				ps.setBoolean(1, false);
				ps.setString(2, URL);
				ps.executeUpdate();
				
				ps = conn.prepareStatement(INSERT_TRANSM_QUERY);
				ps.setString(1, nombre);
				ps.setString(2, descripcion);
				ps.setInt(3, usuario);
				ps.setInt(4, idEstacion);
				ps.executeUpdate();
							
				ps = conn.prepareStatement(GET_TRANSM_QUERY);
				rs = ps.executeQuery(); 
				while(rs.next()) idTransmision = rs.getInt("id");
			}
						
			ConnectionManager.releaseConnection(conn);
			
			Transmision transmision = new Transmision(idTransmision, nombre, descripcion, usuario, URL);
			return transmision;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return null;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}
	
	/*
	 * Parametros: id de la transmision utilizada, URL de la estacion asociada 
	 * Devuelve: false en caso de error, true si se ha finalizado la transmision correctamente
	*/
	public static boolean finalizar(int idTransmision, String url) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_TRANSM_QUERY);
			ps.setInt(1, idTransmision);
			ps.executeUpdate();
			
			ps = conn.prepareStatement(UPDATE_ESTACION_QUERY);
			ps.setBoolean(1, true);
			ps.setString(2, url);
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
	 * Devuelve: una lista de datos de tipo Transmision (id de la misma, nombre,
	 * 		     descipcion, id del usuario, URL de la estación asociada)
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
									rs.getString("descripcion"), rs.getInt("usuario"), rs.getString("url"));
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
	 * Parametros: id de un usuario
	 * Comentarios: Obtiene las transmisiones activas de los usuarios a los que sigue "usuario"
	 * Devuelve: una lista de datos de tipo Transmision (id de la misma, nombre,
	 * 		     descipcion, id del usuario, URL de la estación asociada)
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
										rs2.getString("descripcion"), rs2.getInt("usuario"), rs2.getString("url"));
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
    
    // Prubas con la base de datos
 	public static void main(String[] args) throws SQLException, IOException{
 		/*
 		boolean estacion = anyadirEstacion("http://34.69.44.48/trasnmisiones/trasnm2.ogg");
 		if (estacion) System.out.println("Añadida estacion");
 		
 		boolean estacion2 = borrarEstacion("http://34.69.44.48/trasnmisiones/trasnm.ogg");
 		if (estacion2) System.out.println("Borrada estacion");
 		
 		//String seguir = "INSERT INTO Reproductor_musica.Sigue (usuario1, usuario2) VALUES (?,?)";
 		//Connection conn = ConnectionManager.getConnection();
		//PreparedStatement ps = conn.prepareStatement(seguir);
		//ps.setInt(1, 1);
		//ps.setInt(2, 9);
		//ps.executeUpdate();
		//ConnectionManager.releaseConnection(conn);
		
 		Transmision transmision = iniciar("Capitulo 0101","descripcion",2);
 		if (transmision != null) {
 			System.out.println(transmision.getNombre());
 			System.out.println(transmision.getUrl());
 		}
 		
 		boolean fin = finalizar(2,"http://34.69.44.48/trasnmisiones/trasnm.ogg");
 		if (fin) System.out.println("Transmision finalizada");
 		
 		boolean modificada = cambiar_info("Capitulo nuevo","Nueva descrip",1);
 		if (modificada) System.out.println("Modif transmision");
 		
 		List<Transmision> t = getTransmisionPorNombre("Capitulo nuevo");
 		for (Transmision a : t) {
 			System.out.println(a.getNombre());
 	 		System.out.println(a.getUrl());
 		}
 		
 		List<Transmision> t2 = getTransmisionesUsersSeguidos(1);
 		for (Transmision a : t2) {
 			System.out.println(a.getNombre());
 	 		System.out.println(a.getUrl());
 		}
 		*/
 	}
}
