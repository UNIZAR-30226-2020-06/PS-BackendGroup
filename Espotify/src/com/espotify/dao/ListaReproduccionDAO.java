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

public class ListaReproduccionDAO {
	private final static String ATRIBUTO_PODCAST = "Podcast";
	private final static String INSERT_QUERY = "INSERT INTO Reproductor_musica.ListasRep (usuario, nombre, descripcion, imagen, tipo) VALUES (?,?,?,?,?)";
	private final static String UPDATE_NOM_QUERY = "UPDATE Reproductor_musica.ListasRep SET nombre=? WHERE nombre = ? AND usuario = ? AND tipo = ?";
	private final static String UPDATE_DES_QUERY = "UPDATE Reproductor_musica.ListasRep SET descripcion=? WHERE nombre = ? AND usuario = ? AND tipo = ?";
	private final static String UPDATE_IMG_QUERY = "UPDATE Reproductor_musica.ListasRep SET imagen=? WHERE nombre = ? AND usuario = ? AND tipo = ?";
	private final static String DELETE_QUERY =	"DELETE FROM Reproductor_musica.ListasRep WHERE nombre = ? AND usuario = ? AND tipo = ?";
	private final static String GETINFOLIST_QUERY = "SELECT * FROM Reproductor_musica.ListasRep WHERE nombre = ? AND usuario = ? AND tipo = ?";
	private final static String GETINFOLISTID_QUERY = "SELECT * FROM Reproductor_musica.ListasRep WHERE id = ?";
	private final static String GETAUDIOS_QUERY = "SELECT audio.id id, audio.url url, audio.titulo titulo, user.nombre autor, genero.nombre genero FROM Reproductor_musica.ListasRep lista,"  
													+ "Reproductor_musica.Contiene cont, Reproductor_musica.Audio audio, Reproductor_musica.Genero genero, Reproductor_musica.Usuario user " 
													+ "WHERE lista.id = cont.lista AND cont.audio = audio.id AND audio.genero = genero.id AND audio.usuario = user.id AND "
													+ "lista.nombre = ? AND lista.usuario = ? AND lista.tipo = ? ORDER BY audio.titulo";
	private final static String GETAUDIOSID_QUERY = "SELECT audio.id id, audio.url url, audio.titulo titulo, user.nombre autor, genero.nombre genero FROM Reproductor_musica.ListasRep lista,"  
													+ "Reproductor_musica.Contiene cont, Reproductor_musica.Audio audio, Reproductor_musica.Genero genero, Reproductor_musica.Usuario user " 
													+ "WHERE lista.id = cont.lista AND cont.audio = audio.id AND audio.genero = genero.id AND audio.usuario = user.id AND "
													+ "lista.id = ? ORDER BY audio.titulo";
	private final static String SHOWLISTS_QUERY = "SELECT * FROM Reproductor_musica.ListasRep WHERE usuario = ? AND tipo = ? ORDER BY nombre";
	private final static String SHOW_ALL_LISTS_QUERY = "SELECT * FROM Reproductor_musica.ListasRep WHERE tipo = ? ORDER BY nombre";
	private final static String INSERTAUDIO_QUERY =  "INSERT INTO Reproductor_musica.Contiene (audio, lista) VALUES (?,?)";
	private final static String DELETE_AUDIO_QUERY = "DELETE FROM Reproductor_musica.Contiene WHERE audio = ? AND lista = ?";
	private final static String GETLIST_ID_QUERY = "SELECT id FROM Reproductor_musica.ListasRep WHERE nombre = ?";
	private final static String GETNAMES_QUERY = "SELECT nombre FROM Reproductor_musica.ListasRep WHERE usuario = ? AND tipo = ?";
	private final static String GETALL_PODCAST = "SELECT * FROM Reproductor_musica.ListasRep WHERE tipo = " + ATRIBUTO_PODCAST;
	
	public static boolean crear(int usuario, String nombre, String descripcion, String tipo) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setInt(1, usuario);
			ps.setString(2, nombre);
			ps.setString(3, descripcion);
			ps.setString(4, "");
			ps.setString(5, tipo);
            
            // Asegurar nombre no repetido
            if (!nombreValido(usuario,nombre,tipo)){
            	return false;
            }
            
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
	
	public static boolean crear(String usuario, String nombre, String descripcion, String tipo) {

		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
			
			ps.setString(1, usuario);
			ps.setString(2, nombre);
			ps.setString(3, descripcion);
			ps.setString(4, "");
			ps.setString(5, tipo);
            
            // Asegurar nombre no repetido
            if (!nombreValido(usuario,nombre,tipo)){
            	return false;
            }
            
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
	
	public static boolean borrar(String nombre, String usuario, String tipo) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			
			ps.setString(1, nombre);
	        ps.setString(2, usuario);
	        ps.setString(3, tipo);
	
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
	
	public static boolean borrar(String nombre, int usuario, String tipo) {
		System.out.println("ENTRAREEEEE");
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_QUERY);
			
			ps.setString(1, nombre);
            ps.setInt(2, usuario);
            ps.setString(3, tipo);

			ps.executeUpdate();
			
			ConnectionManager.releaseConnection(conn);
			System.out.println("SALI BN");
			return true;
			
		} catch(SQLException se) {
			System.out.println(se.getMessage());
			System.out.println("SALI MAL");
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}
	
	public static boolean borrarCancionLista(int idCancion, int idLista) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_AUDIO_QUERY);
			
			ps.setInt(1, idCancion);
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
	
	public static boolean quitarAudio(int audio, int lista) {

		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(DELETE_AUDIO_QUERY);

			ps.setInt(1, audio);
            ps.setInt(2, lista);

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
	
	public static boolean cambiar_info(String nombreOld, String nombreNew, int usuario, String descripcion, String imagen, String tipo) {
		System.out.println("VOY A CAMBIAR INFO");
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;
			boolean cambioNombre = false;
			boolean cambiada = false;

			if(nombreNew != null && !nombreNew.equals("") && !nombreNew.equals(nombreOld)) {
				ps = conn.prepareStatement(UPDATE_NOM_QUERY);
				
				ps.setString(1, nombreNew);
				ps.setString(2, nombreOld);
                ps.setInt(3, usuario);
                ps.setString(4, tipo);
				ps.executeUpdate();
				cambioNombre = true;
				cambiada = true;
			}
			if(descripcion != null && !descripcion.equals("")) {
				ps = conn.prepareStatement(UPDATE_DES_QUERY);
				
				ps.setString(1, descripcion);
				if (cambioNombre) {
					ps.setString(2, nombreNew);
				}
				else {
					ps.setString(2, nombreOld);
				}
                ps.setInt(3, usuario);
                ps.setString(4, tipo);
				ps.executeUpdate();
				cambiada = true;
			}
			if(imagen != null && !imagen.equals("")) {
				ps = conn.prepareStatement(UPDATE_IMG_QUERY);
				
				ps.setString(1, imagen);
				if (cambioNombre) {
					ps.setString(2, nombreNew);
				}
				else {
					ps.setString(2, nombreOld);
				}
                ps.setInt(3, usuario);
                ps.setString(4, tipo);
				ps.executeUpdate();
								
				cambiada = true;
			}
			ConnectionManager.releaseConnection(conn);
			return cambiada;
			
		} catch(SQLException se) {
			System.out.println("SQL EXCEPTIONNNNN");
			se.printStackTrace();
			return false;
		} catch(Exception e) {
			e.printStackTrace(System.err);
			return false;
		}
	}

	public static boolean cambiar_info(String nombreOld, String nombreNew, String usuario, String descripcion, String imagen, String tipo) {
	
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps;
			boolean cambioNombre = false;
			boolean cambiada = false;

			if(nombreNew != null && !nombreNew.equals("") && !nombreNew.equals(nombreOld)) {
				ps = conn.prepareStatement(UPDATE_NOM_QUERY);
				
				ps.setString(1, nombreNew);
				ps.setString(2, nombreOld);
                ps.setString(3, usuario);
                ps.setString(4, tipo);
				ps.executeUpdate();
				cambioNombre = true;
				cambiada = true;
			}
			if(descripcion != null && !descripcion.equals("")) {
				ps = conn.prepareStatement(UPDATE_DES_QUERY);
				
				ps.setString(1, descripcion);
				if (cambioNombre) {
					ps.setString(2, nombreNew);
				}
				else {
					ps.setString(2, nombreOld);
				}
                ps.setString(3, usuario);
                ps.setString(4, tipo);
				ps.executeUpdate();
				cambiada = true;
			}
			if(imagen != null && !imagen.equals("")) {
				ps = conn.prepareStatement(UPDATE_IMG_QUERY);
				
				ps.setString(1, imagen);
				if (cambioNombre) {
					ps.setString(2, nombreNew);
				}
				else {
					ps.setString(2, nombreOld);
				}
                ps.setString(3, usuario);
                ps.setString(4, tipo);
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
	
	public List<ListaReproduccion> showLists(int idUsuario, String tipo) {
		List<ListaReproduccion> rutas = new ArrayList<ListaReproduccion>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SHOWLISTS_QUERY);
            
			ps.setInt(1, idUsuario);
            ps.setString(2, tipo);
			
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                rutas.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
	  
			
		} catch(SQLException se) {
			se.printStackTrace();
	
		} catch(Exception e) {
			e.printStackTrace(System.err);
	
		}
		
		return rutas;
	}
	
	public static List<ListaReproduccion> showAllLists(String tipo) {
		List<ListaReproduccion> rutas = new ArrayList<ListaReproduccion>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SHOW_ALL_LISTS_QUERY);
            
            ps.setString(1, tipo);
			
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                rutas.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
	  
			
		} catch(SQLException se) {
			se.printStackTrace();
	
		} catch(Exception e) {
			e.printStackTrace(System.err);
	
		}
		
		return rutas;
	}
	
	
	public List<ListaReproduccion> obtenerTodosPodcast() {
		List<ListaReproduccion> rutas = new ArrayList<ListaReproduccion>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETALL_PODCAST);
			
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                rutas.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
	  
			
		} catch(SQLException se) {
			se.printStackTrace();
	
		} catch(Exception e) {
			e.printStackTrace(System.err);
	
		}
		
		return rutas;
	}
	
	public ListaReproduccion getInfoListId(int idLista) {
		ListaReproduccion result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETINFOLISTID_QUERY);
            
            ps.setInt(1, idLista);			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	public ListaReproduccion getInfoList(String nombre, int usuario, String tipo) {
		ListaReproduccion result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETINFOLIST_QUERY);
            
			ps.setString(1, nombre);
            ps.setInt(2, usuario);
            ps.setString(3, tipo);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	public static ListaReproduccion getInfoList(String nombre, String usuario, String tipo) {
		ListaReproduccion result = null;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETINFOLIST_QUERY);
            
			ps.setString(1, nombre);
            ps.setString(2, usuario);
            ps.setString(3, tipo);
			
			ResultSet rs = ps.executeQuery();

			if(rs.first()){
				result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
			}
			
			ConnectionManager.releaseConnection(conn);
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
		return result;
	}
	
	public static List<Audio> getAudiosId(int idList) {
		List<Audio> audios = new ArrayList<Audio>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETAUDIOSID_QUERY);
            
            ps.setInt(1, idList);
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
	
	public static List<Audio> getAudios(String nombre, int usuario, String tipo) {
		List<Audio> audios = new ArrayList<Audio>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETAUDIOS_QUERY);
            
			ps.setString(1, nombre);
            ps.setInt(2, usuario);
            ps.setString(3, tipo);           
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

	public static List<Audio> getAudios(String nombre, String usuario, String tipo) {
		List<Audio> audios = new ArrayList<Audio>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETAUDIOS_QUERY);
            
			ps.setString(1, nombre);
            ps.setString(2, usuario);
            ps.setString(3, tipo);           
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
	
	

	public static List<ListaReproduccion> showLists(String usuario, String tipo) {
		List<ListaReproduccion> rutas = new ArrayList<ListaReproduccion>();
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SHOWLISTS_QUERY);
   
						 
									 
            
			ps.setString(1, usuario);
            ps.setString(2, tipo);
			
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("usuario"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                rutas.add(result);
			}
			
			ConnectionManager.releaseConnection(conn);
			   
			
		} catch(SQLException se) {
			se.printStackTrace();
				
		} catch(Exception e) {
			e.printStackTrace(System.err);
				
		}
		
		return rutas;
	}
	
	public static boolean anyadirAudio(String idAudio, String idLista) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERTAUDIO_QUERY);
			
			ps.setString(1, idAudio);
			ps.setString(2, idLista);            
            
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
	
	public static boolean anyadirAudio(int idAudio, int idLista) {
		
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERTAUDIO_QUERY);
			
			ps.setInt(1, idAudio);
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
	
	private static boolean nombreValido(int usuario, String nombre, String tipo){
        List<String> nombres = new ArrayList<String>();
        try {
            
            Connection conn2 = ConnectionManager.getConnection();
            PreparedStatement ps = conn2.prepareStatement(GETNAMES_QUERY);
            
            ps.setInt(1, usuario);
            ps.setString(2, tipo);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                String aux = rs.getString("nombre");
                nombres.add(aux);
            }
            
            ConnectionManager.releaseConnection(conn2);
            
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
        
        for (String n : nombres){
            if (n.equals(nombre)){
                return false;
            }
        }
        return true;
    }
    
    // Comprueba si el nombre de una lista de usuario esta repetido (invalido) o no (valido)
    private static boolean nombreValido(String usuario, String nombre, String tipo){
        List<String> nombres = new ArrayList<String>();
        try {
            
            Connection conn2 = ConnectionManager.getConnection();
            PreparedStatement ps = conn2.prepareStatement(GETNAMES_QUERY);
            
            ps.setString(1, usuario);
            ps.setString(2, tipo);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                String aux = rs.getString("nombre");
                nombres.add(aux);
            }
            
            ConnectionManager.releaseConnection(conn2);
            
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
        
        for (String n : nombres){
            if (n.equals(nombre)){
                return false;
            }
        }
        return true;
    }
    
    public static int obtenerIdLista(String nombre) {
    	int id = -1;
		try {

			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GETLIST_ID_QUERY);
            
			ps.setString(1, nombre);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
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
}