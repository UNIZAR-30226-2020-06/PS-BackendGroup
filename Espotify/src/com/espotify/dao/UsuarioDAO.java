package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Usuario;
import com.mysql.cj.jdbc.Blob;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Usuario (mail, descripcion, nombre, password, imagen) VALUES (?,?,?,?,null)";
	private final static String INSERT_IMG_QUERY = "UPDATE Reproductor_musica.Usuario SET imagen=? WHERE mail = ?";
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.Usuario SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.Usuario SET descripcion=? WHERE id = ?";
	private final static String UPDATE_MAIL_QUERY = "UPDATE Reproductor_musica.Usuario SET mail=? WHERE id = ?";
	private final static String UPDATE_IMG_QUERY = "UPDATE Reproductor_musica.Usuario SET imagen=? WHERE id = ?";
	private final static String UPDATE_PASS_QUERY = "UPDATE Reproductor_musica.Usuario SET password=? WHERE id = ? AND password=?";
	private final static String LOGIN_QUERY = "SELECT nombre, descripcion, mail, id, imagen FROM Reproductor_musica.Usuario WHERE mail = ? AND password = ?";
	private final static String USER_GETID_QUERY = "SELECT id, imagen FROM Reproductor_musica.Usuario WHERE mail = ?";
	private final static String USER_GETINFO_QUERY = "SELECT nombre, descripcion, mail FROM Reproductor_musica.Usuario WHERE mail = ?";
	
	
	/**
	 * Registra un usuario nuevo en la base de datos.
	 * @param nombre
	 * @param email
	 * @param contrasena
	 * @param descripcion
	 * @return
	 */
	
	public static boolean register(String nombre,String email, String contrasena, String descripcion, String imagen) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setString(1, email);
			ps.setString(2, descripcion);
			ps.setString(3, nombre);
			
			String pass_HASH = convertirSHA256(contrasena);
			ps.setString(4, pass_HASH);
			ps.executeUpdate();
			
			if(imagen != null && !imagen.equals("")) {
				FileInputStream imagenBinaria = new FileInputStream(imagen);
				ps = conn.prepareStatement(INSERT_IMG_QUERY);
				ps.setBlob(1, imagenBinaria);
				ps.setString(2, email);
				ps.executeUpdate();
				imagenBinaria.close();
			}
			
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

	public static boolean cambiar_info(String nombre, String descripcion, String email, String id, String imagen) {
	
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;

			if(nombre != null && !nombre.equals("")) {
				ps = conn.prepareStatement(UPDATE_NOM_QUERY);
				
				ps.setString(1, nombre);
				ps.setString(2, id);
				ps.executeUpdate();
			}
			if(descripcion != null) {
				ps = conn.prepareStatement(UPDATE_DES_QUERY);
				
				ps.setString(1, descripcion);
				ps.setString(2, id);
				ps.executeUpdate();
			}
			if(email != null && !email.equals("")) {
				ps = conn.prepareStatement(UPDATE_MAIL_QUERY);
				
				ps.setString(1, email);
				ps.setString(2, id);
				ps.executeUpdate();
			}
			if(imagen != null && !imagen.equals("")) {
				ps = conn.prepareStatement(UPDATE_IMG_QUERY);
				FileInputStream imagenBinaria = new FileInputStream(imagen);
				
				ps.setBlob(1, imagenBinaria);
				ps.setString(2, id);
				ps.executeUpdate();
				imagenBinaria.close();
			}
	
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		return false;
	}
	
	
	public static boolean cambiar_pass(String pass1, String pass2, String id) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_PASS_QUERY);

			String pass1_HASH = convertirSHA256(pass1); // password actual
			String pass2_HASH = convertirSHA256(pass2); // password nuevo
			ps.setString(1, pass2_HASH);
			ps.setString(2, id);
			ps.setString(3, pass1_HASH);
	
			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			return true;
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		return false;
	}
	
	
	
	public static String convertirSHA256(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} 
		catch (NoSuchAlgorithmException e) {		
			e.printStackTrace();
			return null;
		}
		    
		byte[] hash = md.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		    
		for(byte b : hash) {        
			sb.append(String.format("%02x", b));
		}
		    
		return sb.toString();
	}
	
	
	public static Usuario login(String email, String contrasena) {
		Usuario result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(LOGIN_QUERY);
			ps.setString(1, email);
			
			// ciframos la contrase�a con HASH256
			String pass_HASH = convertirSHA256(contrasena);
			ps.setString(2, pass_HASH);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new Usuario(rs.getString("nombre"),rs.getString("descripcion"), rs.getString("mail"), rs.getString("id"), (Blob) rs.getBlob("imagen"));
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	public static Usuario obtenerInfo(String email) {
		Usuario result = null;
		Blob blob = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETINFO_QUERY);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new Usuario(rs.getString("nombre"), rs.getString("descripcion"), rs.getString("mail"), null, null);
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	
	public static String obtenerId(String email) {
		String id = "";

		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETID_QUERY);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				id = rs.getString("id");
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return id;
	}
	
	// Prubas con la base de datos
 	public static void main(String[] args) throws SQLException, IOException{
 		/*
 		boolean creado = register("dav","dav@unizar.es","david_password123","descripcion del usuario","/Users/davidallozatejero/downloads/user2.jpg");
 		if (creado) System.out.println("Creado user");
 		
 		boolean modificada = cambiar_info("David AllTej", "asasasas","davidAT@gmail.com","18","/Users/davidallozatejero/downloads/user1.png");
 		if (modificada) System.out.println("Modif user");
 		
 		Usuario u = login("davidAT@gmail.com","david_password123");
 		System.out.println(u.getNombre());
 		System.out.println(u.getDescripcion());
 		*/
 	}
}

