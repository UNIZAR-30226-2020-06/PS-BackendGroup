package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Usuario;
import com.mysql.cj.jdbc.Blob;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDAO {
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Usuario (mail, descripcion, nombre, password, id) VALUES (?,?,?,?,0)";
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.Usuario SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.Usuario SET descripcion=? WHERE id = ?";
	private final static String UPDATE_MAIL_QUERY = "UPDATE Reproductor_musica.Usuario SET mail=? WHERE id = ?";
	private final static String UPDATE_PASS_QUERY = "UPDATE Reproductor_musica.Usuario SET password=? WHERE id = ? AND password=?";
	private final static String LOGIN_QUERY = "SELECT nombre, descripcion, mail, id FROM Reproductor_musica.Usuario WHERE mail = ? AND password = ?";
	
	
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
			Blob blob = (Blob) conn.createBlob();
			blob.setBytes(1, pass_HASH.getBytes());
			
			ps.setBlob(4, blob);
			
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
	
	
	public static boolean cambiar_pass(String pass1, String pass2, String id) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_PASS_QUERY);

			String pass1_HASH = convertirSHA256(pass1); // password actual
			Blob blob1 = (Blob) conn.createBlob();
			blob1.setBytes(1, pass1_HASH.getBytes());
			
			String pass2_HASH = convertirSHA256(pass2); // password nuevo
			Blob blob2 = (Blob) conn.createBlob();
			blob2.setBytes(1, pass2_HASH.getBytes());
			
			ps.setBlob(1, blob2);
			ps.setString(2, id);
			ps.setBlob(3, blob1);
	
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
	
	
	public Usuario login(String email, String contrasena) {
		Usuario result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(LOGIN_QUERY);
			ps.setString(1, email);
			
			// ciframos la contrase�a con HASH256
			String pass_HASH = convertirSHA256(contrasena);
			Blob blob = (Blob) conn.createBlob();
			blob.setBytes(1, pass_HASH.getBytes());
			
			ps.setBlob(2, blob);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new Usuario(rs.getString("nombre"),rs.getString("descripcion"), rs.getString("mail"), rs.getString("id"));
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
}
