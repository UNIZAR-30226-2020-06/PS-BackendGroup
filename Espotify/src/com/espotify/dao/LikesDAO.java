package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import java.io.IOException;


public class LikesDAO {

	private final static String INSERT_LIKESAUDIO_QUERY = "INSERT INTO Reproductor_musica.LikesAudio (usuario, audio) VALUES (?,?)";
	private final static String INSERT_LIKESLISTA_QUERY = "INSERT INTO Reproductor_musica.LikesLista (usuario, lista) VALUES (?,?)";
	private final static String INSERT_LIKESTRANS_QUERY = "INSERT INTO Reproductor_musica.LikesTrans (usuario, transmision) VALUES (?,?)";

	private final static String DELETE_LIKESAUDIO_QUERY =	"DELETE FROM Reproductor_musica.LikesAudio WHERE usuario = ? AND audio = ?";
	private final static String DELETE_LIKESLISTA_QUERY =	"DELETE FROM Reproductor_musica.LikesLista WHERE usuario = ? AND lista = ?";
	private final static String DELETE_LIKESTRANS_QUERY =	"DELETE FROM Reproductor_musica.LikesTrans WHERE usuario = ? AND transmision = ?";
	
	// --------------------------------------------------------------------------
	//	IDEA DE USO DE LAS FUNCIONES: 
	//		Cada vez que se pulse el boton de like se ejecutará primero 
	//		la función de añadir, si el resultado es falso entonces se 
	//		ejecutará la de eliminar.
	// --------------------------------------------------------------------------
	
	/*
	 * Parametros: id del usuario, id del audio
	 * Devuelve: falso si ya se le había dado like, true si el like se ha dado correctamente 
	*/
	public static boolean anyadirLikeAudio(int usuario, int idAudio) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(INSERT_LIKESAUDIO_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idAudio);
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
	 * Parametros: id del usuario, id de la lista de reproducción
	 * Devuelve: falso si ya se le había dado like, true si el like se ha dado correctamente 
	*/
	public static boolean anyadirLikeLista(int usuario, int idLista) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(INSERT_LIKESLISTA_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idLista);
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
	 * Parametros: id del usuario, id de la transmision en vivo
	 * Devuelve: falso si ya se le había dado like, true si el like se ha dado correctamente 
	*/
	public static boolean anyadirLikeTrans(int usuario, int idTransmision) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(INSERT_LIKESTRANS_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idTransmision);
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
	 * Parametros: id del usuario, id del audio
	 * Devuelve: falso si ya se había quitado el like, true si el like se ha quitado correctamente 
	*/
	public static boolean quitarLikeAudio(int usuario, int idAudio) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(DELETE_LIKESAUDIO_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idAudio);
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
	 * Parametros: id del usuario, id de la lista de reproducción
	 * Devuelve: falso si ya se había quitado el like, true si el like se ha quitado correctamente  
	*/
	public static boolean quitarLikeLista(int usuario, int idLista) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(DELETE_LIKESLISTA_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idLista);
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
	 * Parametros: id del usuario, id de la transmision en vivo
	 * Devuelve: falso si ya se había quitado el like, true si el like se ha quitado correctamente 
	*/
	public static boolean quitarLikeTrans(int usuario, int idTransmision) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(DELETE_LIKESTRANS_QUERY);
			ps.setInt(1, usuario);
			ps.setInt(2, idTransmision);
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
	
    // Prubas con la base de datos
 	public static void main(String[] args) throws SQLException, IOException{
 		/*
 		boolean anyadido,quitado = false;
 		
 		anyadido = anyadirLikeAudio(1,1);
 		if (anyadido) System.out.println("Like Audio añadido");
 		
 		anyadido = anyadirLikeLista(1,14);
 		if (anyadido) System.out.println("Like Lista añadido");
 		
 		anyadido = anyadirLikeTrans(1,1);
 		if (anyadido) System.out.println("Like Trasnmision añadido");
 		
 		// ----------------------------------------------------------
 		
 		quitado = quitarLikeAudio(1,1);
 		if (quitado) System.out.println("Like Audio quitado");
 		
 		quitado = quitarLikeLista(1,14);
 		if (quitado) System.out.println("Like Lista quitado");
 		
 		quitado = quitarLikeTrans(1,1);
 		if (quitado) System.out.println("Like Transmision quitado");
 		*/
 	}
}
