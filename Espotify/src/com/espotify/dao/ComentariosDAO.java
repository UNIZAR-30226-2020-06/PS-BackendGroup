package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.espotify.model.ConnectionManager;
import com.espotify.model.Transmision;
import com.espotify.model.Comentario;
import java.io.IOException;


public class ComentariosDAO {

	private final static String INSERT_COMENTARIO_QUERY = "INSERT INTO Reproductor_musica.Comentario (descripcion, tipo, usuario, transmision, audio) VALUES (?,?,?,?,?)";
	
	// Solo debe usarse para los de tipo "audio", nunca para el tipo "transmision"
	private final static String DELETE_COMENTARIO_QUERY = "DELETE FROM Reproductor_musica.Comentario WHERE id = ?";
	
	private final static String GETULTIMO_COMENTARIOAUDIO_QUERY ="SELECT com.id id, com.descripcion descrip, usr.nombre nameUsr FROM Reproductor_musica.Comentario com, Reproductor_musica.Usuario usr "
			 													 +"WHERE com.usuario = usr.id AND com.tipo = ? AND com.audio = ? ORDER BY id DESC LIMIT 1";
	private final static String GETULTIMO_COMENTARIOTRANS_QUERY ="SELECT com.id id, com.descripcion descrip, usr.nombre nameUsr FROM Reproductor_musica.Comentario com, Reproductor_musica.Usuario usr "
			 													 +"WHERE com.usuario = usr.id AND com.tipo = ? AND com.transmision = ? ORDER BY id DESC LIMIT 1";
	private final static String GET_COMENTARIOSAUDIO_QUERY = "SELECT com.id id, com.descripcion descrip, usr.nombre nameUsr FROM Reproductor_musica.Comentario com, Reproductor_musica.Usuario usr "
													 	     +"WHERE com.usuario = usr.id AND com.tipo = ? AND com.audio = ?";
	private final static String GET_COMENTARIOSTRANS_QUERY = "SELECT com.id id, com.descripcion descrip, usr.nombre nameUsr FROM Reproductor_musica.Comentario com, Reproductor_musica.Usuario usr "
		 													 +"WHERE com.usuario = usr.id AND com.tipo = ? AND com.transmision = ?";

	/*
	 * Parametros: descripcion del comentario, id del usuario, id del audio
	 * Devuelve: falso si ha habido un error, true si se ha almacenado el comentario correctamente para el audio
	*/
	public static boolean anyadirComentarioAudio(String descripcion, int idUsuario, int idAudio) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(INSERT_COMENTARIO_QUERY);
			ps.setString(1, descripcion);
			ps.setString(2, "audio");
			ps.setInt(3, idUsuario);
			ps.setString(4, null); // Dato nulo
			ps.setInt(5, idAudio);
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
	 * Parametros: descripcion del comentario, id del usuario, id de la transmision
	 * Devuelve: falso si ha habido un error, true si se ha almacenado el comentario correctamente para la transmision
	*/
	public static boolean anyadirComentarioTrans(String descripcion, int idUsuario, int idTrans) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(INSERT_COMENTARIO_QUERY);
			ps.setString(1, descripcion);
			ps.setString(2, "transmision");
			ps.setInt(3, idUsuario);
			ps.setInt(4, idTrans);
			ps.setString(5, null); // Dato nulo
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
	 * Parametros: id del comentario de un audio
	 * Devuelve: falso si ha habido un error, true si se ha eliminado el comentario de un audio correctamente
	*/
	public static boolean quitarComentarioAudio(int idComentario) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
						
			PreparedStatement ps = conn.prepareStatement(DELETE_COMENTARIO_QUERY);
			ps.setInt(1, idComentario);
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
	 * Parametros: id del audio de donde obtener el ultimo comentario
	 * Devuelve: El último comentario añadido a un audio en un dato de tipo Comentario 
	 * 			 (id del comentario, descripcion del comentario, nombre del autor del comentario)
	*/
	public static Comentario getUltimoComentarioAudio(int idAudio) {
		Comentario comentario = null;
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETULTIMO_COMENTARIOAUDIO_QUERY);
			ps.setString(1, "audio");
			ps.setInt(2, idAudio);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				comentario = new Comentario(rs.getInt("id"), rs.getString("descrip"), rs.getString("nameUsr"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return comentario;
	}
	
	/*
	 * Parametros: id de la transmision de donde obtener el ultimo comentario
	 * Devuelve: El último comentario añadido a una transmision en un dato de tipo Comentario 
	 * 			 (id del comentario, descripcion del comentario, nombre del autor del comentario)
	*/
	public static Comentario getUltimoComentarioTrans(int idTransmision) {
		Comentario comentario = null;
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETULTIMO_COMENTARIOTRANS_QUERY);
			ps.setString(1, "transmision");
			ps.setInt(2, idTransmision);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				comentario = new Comentario(rs.getInt("id"), rs.getString("descrip"), rs.getString("nameUsr"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return comentario;
	}
	
	/*
	 * Parametros: id de un Audio
	 * Devuelve: una lista de datos de tipo Comentario 
	 * 			 (id del comentario, descripcion del comentario, nombre del autor del comentario)
	*/
	public static List<Comentario> getComentariosAudio(int idAudio) {
		List<Comentario> comentarios = new ArrayList<Comentario>();
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_COMENTARIOSAUDIO_QUERY);
			ps.setString(1, "audio");
			ps.setInt(2, idAudio);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Comentario result = new Comentario(rs.getInt("id"), rs.getString("descrip"), rs.getString("nameUsr"));
                comentarios.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return comentarios;
	}
	
	/*
	 * Parametros: id de una Transmisión
	 * Devuelve: una lista de datos de tipo Comentario 
	 * 			 (id del comentario, descripcion del comentario, nombre del autor del comentario)
	*/
	public static List<Comentario> getComentariosTrans(int idTransmision) {
		List<Comentario> comentarios = new ArrayList<Comentario>();
		try {
			
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_COMENTARIOSTRANS_QUERY);
			ps.setString(1, "transmision");
			ps.setInt(2, idTransmision);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Comentario result = new Comentario(rs.getInt("id"), rs.getString("descrip"), rs.getString("nameUsr"));
                comentarios.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return comentarios;
	}
	
    // Prubas con la base de datos
 	public static void main(String[] args) throws SQLException, IOException{
 		/*
 		boolean anyadir = anyadirComentarioAudio("Comentario 1",1,1);
 		if (anyadir) System.out.println("Comentario añadido al audio");
 		
 		boolean anyadir2 = anyadirComentarioTrans("Comentario 2",1,1);
 		if (anyadir2) System.out.println("Comentario añadido a la transmision");
 		
 		boolean quitar = quitarComentarioAudio(4);
 		if (quitar) System.out.println("Comentario del audio quitado");
 		
 		Comentario cA = getUltimoComentarioAudio(1);
 		if (cA != null) System.out.println(cA.getDescripcion());
 		if (cA != null) System.out.println(cA.getId());
 		
 		Comentario cT = getUltimoComentarioTrans(1);
 		if (cT != null) System.out.println(cT.getDescripcion());
 		if (cT != null) System.out.println(cT.getId());
 		
 		List<Comentario> comsA = getComentariosAudio(1);
 		for (Comentario c : comsA) {
 			System.out.println(c.getDescripcion());
 		}
 		
 		List<Comentario> comsT = getComentariosTrans(1);
 		for (Comentario c : comsT) {
 			System.out.println(c.getDescripcion());
 		}
 		*/
 	}
}

