package com.espotify.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Part;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class UsuarioDAO {
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.Usuario (mail, descripcion, nombre, password, imagen) VALUES (?,?,?,?,null)";
	private final static String DELETE_QUERY = "DELETE FROM Reproductor_musica.Usuario WHERE id = ?";
	private final static String INSERT_IMG_QUERY = "UPDATE Reproductor_musica.Usuario SET imagen=? WHERE mail = ?";
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.Usuario SET nombre=? WHERE id = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.Usuario SET descripcion=? WHERE id = ?";
	private final static String UPDATE_MAIL_QUERY = "UPDATE Reproductor_musica.Usuario SET mail=? WHERE id = ?";
	private final static String UPDATE_IMG_QUERY = "UPDATE Reproductor_musica.Usuario SET imagen = ? WHERE id = ?";
	private final static String UPDATE_PASS_QUERY = "UPDATE Reproductor_musica.Usuario SET password=? WHERE id = ? AND password=?";
	private final static String UPDATE_PASS_QUERY_NOVERIFY = "UPDATE Reproductor_musica.Usuario SET password=? WHERE id = ?";
	private final static String LOGIN_QUERY = "SELECT nombre, descripcion, mail, id, imagen FROM Reproductor_musica.Usuario WHERE nombre = ? AND password = ?";
	private final static String USER_GETID_QUERY = "SELECT id, imagen FROM Reproductor_musica.Usuario WHERE mail = ?";
	private final static String USER_GETID_NAME_QUERY = "SELECT id FROM Reproductor_musica.Usuario WHERE nombre = ?";
	private final static String USER_GETINFO_QUERY = "SELECT nombre, descripcion, mail, imagen FROM Reproductor_musica.Usuario WHERE id = ?";
	private final static String USER_GETIMAGE_BLOB_QUERY = "SELECT imagen FROM Reproductor_musica.Usuario WHERE mail = ?";
	private final static String USER_GETALL_QUERY = "SELECT * FROM Reproductor_musica.Usuario";
	private final static String ALMACEN_IMG_URL = "https://espotify.ddns.net/almacen-mp3/almacen-img/usuarios/";
	
	/**
	 * Registra un usuario nuevo en la base de datos.
	 * @param nombre
	 * @param email
	 * @param contrasena
	 * @param descripcion
	 * @return
	 */
	
	public static boolean register(String nombre,String email, String contrasena, String descripcion, String imagen) {
		String rutaImagen = ALMACEN_IMG_URL;
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setString(1, email);
			ps.setString(2, descripcion);
			ps.setString(3, nombre);
			
			String pass_HASH = convertirSHA256(contrasena);
			ps.setString(4, pass_HASH);
			ps.executeUpdate();
			
			String idImagen = obtenerIdDesdeEmail(email);
			rutaImagen += idImagen + ".jpg";
			
			ps = conn.prepareStatement(INSERT_IMG_QUERY);
			ps.setString(1, rutaImagen);
			ps.setString(2, email);
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
	
	// Elimina la info de un usuario de la BD, y por consiguiente toda su información asociada en cascada
	// True eliminado correctamente, False ha habido un error
	// Parametro = idUser (id del usuario a eliminar)
	public static boolean eliminar(String idUser) {
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			ps.setString(1, idUser);
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
				
				ps.setString(1, imagen);
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
	
	public static boolean actualizarImagenUsuario(String id, String imagen) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;
			
			if(imagen != null && !imagen.equals("")) {
				ps = conn.prepareStatement(UPDATE_IMG_QUERY);
				
				ps.setString(1, imagen);
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
	
	public static boolean actualizarImagenLista(String id, String imagen) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;
			
			if(imagen != null && !imagen.equals("")) {
				ps = conn.prepareStatement(UPDATE_IMG_QUERY);
				
				ps.setString(1, imagen);
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
			String pass2_HASH = convertirSHA256(pass2); // password nuevo
			ps.setString(1, pass2_HASH);
			ps.setString(2, id);
			ps.setString(3, pass1_HASH);
	
			if(ps.executeUpdate()==1) { // Se ha podido insertar
				ConnectionManager.releaseConnection(conn);
				return true;
			}
			else {
				ConnectionManager.releaseConnection(conn);
				return false;
			}
		} catch(SQLException se) {
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		return false;
	}
	
	public static boolean cambiar_pass_noverify(String nuevaContrasenya, String id) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(UPDATE_PASS_QUERY_NOVERIFY);

			String pass1_HASH = convertirSHA256(nuevaContrasenya); // password actual
			ps.setString(1, pass1_HASH);
			ps.setString(2, id);

			if(ps.executeUpdate()==1) { // Se ha podido insertar
				ConnectionManager.releaseConnection(conn);
				return true;
			}
			else {
				ConnectionManager.releaseConnection(conn);
				return false;
			}
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
	
	
	public static Usuario login(String usuario, String contrasena) {
		Usuario result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(LOGIN_QUERY);
			ps.setString(1, usuario);
			
			// ciframos la contrasea con HASH256
			String pass_HASH = convertirSHA256(contrasena);
			ps.setString(2, pass_HASH);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new Usuario(rs.getString("nombre"),rs.getString("descripcion"), rs.getString("mail"), rs.getString("id"), rs.getString("imagen"));
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	public static Usuario obtenerInfo(int id) {
		Usuario result = null;
		Blob blob = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETINFO_QUERY);
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new Usuario(rs.getString("nombre"), rs.getString("descripcion"), rs.getString("mail"), null, rs.getString("imagen"));
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	
	public static String obtenerIdDesdeEmail(String email) {
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
	
	public static int obtenerIdDesdeNombreUsuario(String nombreUsuario) {
		int id = -1;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETID_NAME_QUERY);
			ps.setString(1, nombreUsuario);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				id = rs.getInt("id");
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		return id;
	}
	
	
	
	public static String obtenerURLImagen(String email) {
		String imagen = null;
		try {
	
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETIMAGE_BLOB_QUERY);
			ps.setString(1, email);
			
			ResultSet rs = ps.executeQuery();
	
			if(rs.first()){
				imagen = rs.getString("imagen");
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return imagen;
	}
	
	public static List<Usuario> obtenerTodosUsuarios() {
		List<Usuario> listaUsuarios = new ArrayList<>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(USER_GETALL_QUERY);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				listaUsuarios.add(new Usuario(rs.getString(2), rs.getString(3), rs.getString(5) , null, rs.getString(6)));
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return listaUsuarios;

	}

}
