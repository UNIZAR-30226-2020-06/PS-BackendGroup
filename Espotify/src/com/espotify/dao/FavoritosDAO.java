package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.ConnectionManager;
import com.espotify.model.ListaReproduccion;
import com.mysql.cj.jdbc.Blob;
import com.espotify.model.Audio;

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

public class FavoritosDAO {
	private final static String GETAUDIOS_QUERY = "SELECT audio.id id, audio.url url, audio.titulo titulo, user.nombre autor, genero.nombre genero FROM "  
													+ "Reproductor_musica.Favoritos fav, Reproductor_musica.Audio audio, Reproductor_musica.Genero genero, Reproductor_musica.Usuario user " 
													+ "WHERE fav.audio = audio.id AND audio.genero = genero.id AND audio.usuario = user.id AND "
													+ "fav.usuario = ? ORDER BY audio.titulo";
	private final static String INSERTAUDIO_QUERY =  "INSERT INTO Reproductor_musica.Favoritos (usuario, audio) VALUES (?,?)";
	private final static String DELETEAUDIO_QUERY = "DELETE FROM Reproductor_musica.Favoritos WHERE usuario = ? AND audio = ?";


	public List<Audio> getAudios(int usuario) {
		List<Audio> audios = new ArrayList<Audio>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETAUDIOS_QUERY);
            
            ps.setInt(1, usuario);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Audio result = new Audio(rs.getString("id"), rs.getString("url"), 
						rs.getString("titulo"), rs.getString("autor"), rs.getString("genero"));
                audios.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return audios;
	}
	
	public  boolean anyadirAudio(int usuario, int idAudio) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERTAUDIO_QUERY);
			
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
	
	public  boolean quitarAudio(int usuario, int audio) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETEAUDIO_QUERY);
			
			ps.setInt(1, usuario);
			ps.setInt(2, audio);            
            
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
 	//public static void main(String[] args) throws SQLException, IOException{
 		
 		//boolean anyadido = anyadirAudio("2","6");
 		//if (anyadido) System.out.println("Añadido audio a favoritos");
 		
 		/*List<Audio> la = getAudios("2");
 		for (Audio a : la) {
 			System.out.println(a.getTitulo());
 	 		System.out.println(a.getGenero());
 	 		System.out.println(a.getUsuario());
 		}*/
 		
 	//}
}