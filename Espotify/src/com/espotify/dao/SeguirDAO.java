package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Usuario;
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

public class SeguirDAO {
	
	private final static String INSERT_SEGUIR_QUERY = "INSERT INTO Reproductor_musica.Sigue (usuario1, usuario2) VALUES (?,?)";
	
	private final static String DELETE_SEGUIR_QUERY =	"DELETE FROM Reproductor_musica.Sigue WHERE usuario1 = ? AND usuario2 = ?";

	private final static String GET_USERSSEGUIDOS_QUERY = "SELECT usuario.id id, usuario.nombre nombre, usuario.descripcion descripcion, usuario.mail mail, usuario.imagen imagen "
														   +"FROM Reproductor_musica.Sigue sigue, Reproductor_musica.Usuario usuario "
														   +"WHERE sigue.usuario2 = usuario.id AND sigue.usuario1 = ?";
	private final static String GET_USERSSEGUIDORES_QUERY = "SELECT usuario.id id, usuario.nombre nombre, usuario.descripcion descripcion, usuario.mail mail, usuario.imagen imagen "
			   											  +"FROM Reproductor_musica.Sigue sigue, Reproductor_musica.Usuario usuario "
			   											  +"WHERE sigue.usuario1 = usuario.id AND sigue.usuario2 = ?";
		
	/*
	 * Parametros: id del usuario propio (usuario1), id del usuario a seguir (usuario2)
	 * Devuelve: false en caso de error, true si se ha seguido al usuario2 correctamente
	*/
	public static boolean followUser(int usuario1, int usuario2) {
		if (usuario1 == usuario2) return false; // Evitar seguirte a ti mismo
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_SEGUIR_QUERY);
			ps.setInt(1, usuario1);
			ps.setInt(2, usuario2);
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
	 * Parametros: id del usuario propio (usuario1), id del usuario a dejar de seguir (usuario2)
	 * Devuelve: false en caso de error, true si se ha dejado de seguir al usuario2 correctamente
	*/
	public static boolean unfollowUser(int usuario1, int usuario2) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_SEGUIR_QUERY);
			ps.setInt(1, usuario1);
			ps.setInt(2, usuario2);
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
	 * Parametros: id del usuario propio
	 * Devuelve: una lista de datos tipo Usuario con los usuarios que sigues 
	 * 			 (nombre del usuario, descripcion, eMail, id del usuario, bytes de la imagen)
	*/
	public static List<Usuario> getFollows(int idUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_USERSSEGUIDOS_QUERY);
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Usuario result = new Usuario(rs.getString("nombre"),rs.getString("descripcion"),
									rs.getString("mail"),rs.getString("id"),rs.getString("imagen"));
                usuarios.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return usuarios;
	}
	
	/*
	 * Parametros: id del usuario propio
	 * Devuelve: una lista de datos tipo Usuario con los usuarios que te siguen 
	 * 			 (nombre del usuario, descripcion, eMail, id del usuario, bytes de la imagen)
	*/
	public static List<Usuario> getFollowers(int idUsuario) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_USERSSEGUIDORES_QUERY);
			ps.setInt(1, idUsuario);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Usuario result = new Usuario(rs.getString("nombre"),rs.getString("descripcion"),
									rs.getString("mail"),rs.getString("id"),rs.getString("imagen"));
                usuarios.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return usuarios;
	}
    
    // Prubas con la base de datos
 	public static void main(String[] args) throws SQLException, IOException{
 		
 		//boolean seguido = followUser(1,10);
 		//if (seguido) System.out.println("Se ha seguido a un usuario");
 		
 		//boolean noseguir = unfollowUser(1,10);
 		//if (noseguir) System.out.println("Se ha dejado de seguir a un usuario");
 		
 		//List<Usuario> users1 = getFollows(1);
 		//for (Usuario u : users1) System.out.println(u.getNombre());
 		
 		//List<Usuario> users2 = getFollowers(2);
 		//for (Usuario u : users2) System.out.println(u.getNombre());
 		
 	}
}

