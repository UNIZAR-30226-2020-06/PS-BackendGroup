package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Usuario (mail, descripcion, nombre, password, id) VALUES (?,?,?,?,0)";
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.Usuario SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.Usuario SET descripcion=? WHERE id = ?";
	private final static String UPDATE_MAIL_QUERY = "UPDATE Reproductor_musica.Usuario SET mail=? WHERE id = ?";
	
	/**
	 * Registra un usuario nuevo en la base de datos.
	 * @param nombre
	 * @param email
	 * @param contrasena
	 * @param descripcion
	 * @return
	 */
	
	public boolean register(String nombre,String email, String contrasena, String descripcion) {
		
		try {
			Connection conn = ConnectionManager.getConnection();

			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setString(1, email);
			ps.setString(2, descripcion);
			ps.setString(3, nombre);
			
			String pass_HASH = convertirSHA256(contrasena);
			ps.setString(4, pass_HASH);
			
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

	public static boolean cambiar_info(String nombre, String descripcion, String email, String id) {
	
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;

			if(nombre != null && !nombre.equals("")) {
				ps = conn.prepareStatement(UPDATE_NOM_QUERY);
				
				ps.setString(1, nombre);
				ps.setString(2, id);
				ps.executeUpdate();
			}
			else if(descripcion != null) {
				ps = conn.prepareStatement(UPDATE_DES_QUERY);
				
				ps.setString(1, descripcion);
				ps.setString(2, id);
				ps.executeUpdate();
			}
			else if(email != null && !email.equals("")) {
				ps = conn.prepareStatement(UPDATE_MAIL_QUERY);
				
				ps.setString(1, email);
				ps.setString(2, id);
				ps.executeUpdate();
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
}

