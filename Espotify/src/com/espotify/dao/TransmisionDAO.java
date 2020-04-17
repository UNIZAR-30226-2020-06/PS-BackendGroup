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
	private final static String INSERT_ESTACION_QUERY = "INSERT INTO Reproductor_musica.Estacion (url) VALUES (?)";
	private final static String INSERT_TRANSM_QUERY = "INSERT INTO Reproductor_musica.TransmisionVivo (nombre, descripcion, usuario, estacion) VALUES (?,?,?,?)";
	private final static String GET_ESTACION_QUERY = "SELECT id FROM Reproductor_musica.Estacion ORDER BY id DESC LIMIT 1";
	private final static String GET_TRANSM_QUERY = "SELECT id FROM Reproductor_musica.TransmisionVivo ORDER BY id DESC LIMIT 1";
			
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.TransmisionVivo SET descripcion=? WHERE id = ?";
	
	private final static String DELETE_ESTACION_QUERY =	"DELETE FROM Reproductor_musica.Estacion WHERE url = ?";
	private final static String DELETE_TRANSM_QUERY =	"DELETE FROM Reproductor_musica.TransmisionVivo WHERE id = ?";
	
	private final static String GET_TRANSM_NOMBRE_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.nombre = ?";
	private final static String GET_TRANSM_USERS_QUERY = "SELECT transmision.id id, transmision.nombre nombre, transmision.descripcion descripcion, transmision.usuario usuario, estacion.url url " 
															+ "FROM Reproductor_musica.TransmisionVivo transmision, Reproductor_musica.Estacion estacion "
															+ "WHERE transmision.estacion = estacion.id AND transmision.usuario = ?";
	private final static String GET_USERS_SEGUIDOS_QUERY = "SELECT usuario2 FROM Reproductor_musica.Sigue WHERE usuario1 = ?";
	
	
	public static int iniciar(String nombre, String descripcion, int usuario, String url) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_ESTACION_QUERY);
			ps.setString(1, url);
			ps.executeUpdate();
						
			ps = conn.prepareStatement(GET_ESTACION_QUERY);
			ResultSet rs = ps.executeQuery();
			int idEstacion = 0; 
			while(rs.next()) idEstacion = rs.getInt("id");
						
			ps = conn.prepareStatement(INSERT_TRANSM_QUERY);
			ps.setString(1, nombre);
			ps.setString(2, descripcion);
			ps.setInt(3, usuario);
			ps.setInt(4, idEstacion);
			ps.executeUpdate();
						
			ps = conn.prepareStatement(GET_TRANSM_QUERY);
			rs = ps.executeQuery();
			int idTransmision = 0; 
			while(rs.next()) idTransmision = rs.getInt("id");
						
			ConnectionManager.releaseConnection(conn);
			return idTransmision;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			return -1;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return -1;
		}
	}
	
	public static boolean finalizar(int idTransmision) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_TRANSM_QUERY);
			
			ps.setInt(1, idTransmision);

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
	
	public static boolean cerrarEstacionTransmisiones(String url) {
		
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
 		String seguir = "INSERT INTO Reproductor_musica.Sigue (usuario1, usuario2) VALUES (?,?)";
 		Connection conn = ConnectionManager.getConnection();
		PreparedStatement ps = conn.prepareStatement(seguir);
		ps.setInt(1, 1);
		ps.setInt(2, 9);
		ps.executeUpdate();
		ConnectionManager.releaseConnection(conn);
		
 		//int iniciada = iniciar("Capitulo 91","descripcion",9,"http:://asdf");
 		//if (iniciada != -1) System.out.println("Trans vivo iniciada"+iniciada);
 		
 		//boolean borrada = finalizar(2);
 		//if (borrada) System.out.println("Transmision finalizada");
 		
 		//boolean borrada2 = cerrarEstacionTransmisiones("http:://asdf");
 		//if (borrada2) System.out.println("Transmision finalizada");
 		
 		//boolean modificada = cambiar_info("Capitulo 12","Nueva descrip",1);
 		//if (modificada) System.out.println("Modif transmision");
 		
 		List<Transmision> t = getTransmisionPorNombre("Capitulo 21");
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

