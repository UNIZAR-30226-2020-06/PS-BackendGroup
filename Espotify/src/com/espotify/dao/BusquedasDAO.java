package com.espotify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.espotify.model.Audio;
import com.espotify.model.ConnectionManager;
import com.espotify.model.ListaReproduccion;
import com.espotify.model.Transmision;
import com.espotify.model.Usuario;
import com.mysql.cj.jdbc.Blob;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class BusquedasDAO {
	private final static String SEARCH_CANCIONES_QUERY = "(SELECT COUNT(*) sumaLikes, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
														+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.LikesAudio likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
														+ "WHERE audios.id = likes.audio AND audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'cancion' AND audios.titulo LIKE ? "
														+ "GROUP BY likes.audio ORDER BY sumaLikes DESC)"
														+ " UNION "
														+ "(SELECT @sumaLikes=0, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
														+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
														+ "WHERE audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'cancion' AND audios.titulo LIKE ? "
														+ "AND audios.id NOT IN (SELECT audio FROM Reproductor_musica.LikesAudio))"
														+ " LIMIT 5";
	private final static String SEARCH_CANCIONES = "(SELECT COUNT(*) sumaLikes, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
												+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.LikesAudio likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
												+ "WHERE audios.id = likes.audio AND audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'cancion' AND audios.titulo LIKE ? "
												+ "GROUP BY likes.audio ORDER BY sumaLikes DESC)"
												+ " UNION "
												+ "(SELECT @sumaLikes=0, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
												+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
												+ "WHERE audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'cancion' AND audios.titulo LIKE ? "
												+ "AND audios.id NOT IN (SELECT audio FROM Reproductor_musica.LikesAudio))";
	
	private final static String SEARCH_CAPITULOS_QUERY = "(SELECT COUNT(*) sumaLikes, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
														+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.LikesAudio likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
														+ "WHERE audios.id = likes.audio AND audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'capituloPodcast' AND audios.titulo LIKE ? "
														+ "GROUP BY likes.audio ORDER BY sumaLikes DESC)"
														+ " UNION "
														+ "(SELECT @sumaLikes=0, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
														+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
														+ "WHERE audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'capituloPodcast' AND audios.titulo LIKE ? "
														+ "AND audios.id NOT IN (SELECT audio FROM Reproductor_musica.LikesAudio))"
														+ " LIMIT 5";
	private final static String SEARCH_CAPITULOS = "(SELECT COUNT(*) sumaLikes, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
												+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.LikesAudio likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
												+ "WHERE audios.id = likes.audio AND audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'capituloPodcast' AND audios.titulo LIKE ? "
												+ "GROUP BY likes.audio ORDER BY sumaLikes DESC)"
												+ " UNION "
												+ "(SELECT @sumaLikes=0, audios.id id, audios.titulo titulo, audios.url url, usuarios.nombre nomUser, generos.nombre nomGenero "
												+ "FROM Reproductor_musica.Audio audios, Reproductor_musica.Usuario usuarios, Reproductor_musica.Genero generos "
												+ "WHERE audios.genero = generos.id AND audios.usuario = usuarios.id AND generos.tipo = 'capituloPodcast' AND audios.titulo LIKE ? "
												+ "AND audios.id NOT IN (SELECT audio FROM Reproductor_musica.LikesAudio))";
	
	private final static String SEARCH_LISTASREP_QUERY = "(SELECT COUNT(*) sumaLikes, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
														+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.LikesLista likes, Reproductor_musica.Usuario usuarios "
														+ "WHERE listas.id = likes.lista AND listas.usuario = usuarios.id AND listas.tipo = 'ListaRep' AND listas.nombre LIKE ? "
														+ "GROUP BY likes.lista ORDER BY sumaLikes DESC)"
														+ " UNION "
														+ "(SELECT @sumaLikes=0, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
														+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.Usuario usuarios "
														+ "WHERE listas.usuario = usuarios.id AND listas.tipo = 'ListaRep' AND listas.nombre LIKE ? "
														+ "AND listas.id NOT IN (SELECT lista FROM Reproductor_musica.LikesLista))"
														+ " LIMIT 5";
	private final static String SEARCH_LISTAREP = "(SELECT COUNT(*) sumaLikes, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
												+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.LikesLista likes, Reproductor_musica.Usuario usuarios "
												+ "WHERE listas.id = likes.lista AND listas.usuario = usuarios.id AND listas.tipo = 'ListaRep' AND listas.nombre LIKE ? "
												+ "GROUP BY likes.lista ORDER BY sumaLikes DESC)"
												+ " UNION "
												+ "(SELECT @sumaLikes=0, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
												+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.Usuario usuarios "
												+ "WHERE listas.usuario = usuarios.id AND listas.tipo = 'ListaRep' AND listas.nombre LIKE ? "
												+ "AND listas.id NOT IN (SELECT lista FROM Reproductor_musica.LikesLista))";
	
	private final static String SEARCH_PODCASTS_QUERY = "(SELECT COUNT(*) sumaLikes, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
													+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.LikesLista likes, Reproductor_musica.Usuario usuarios "
													+ "WHERE listas.id = likes.lista AND listas.usuario = usuarios.id AND listas.tipo = 'podcast' AND listas.nombre LIKE ? "
													+ "GROUP BY likes.lista ORDER BY sumaLikes DESC)"
													+ " UNION "
													+ "(SELECT @sumaLikes=0, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
													+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.Usuario usuarios "
													+ "WHERE listas.usuario = usuarios.id AND listas.tipo = 'podcast' AND listas.nombre LIKE ? "
													+ "AND listas.id NOT IN (SELECT lista FROM Reproductor_musica.LikesLista))"
													+ " LIMIT 5";
	private final static String SEARCH_PODCASTS = "(SELECT COUNT(*) sumaLikes, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
												+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.LikesLista likes, Reproductor_musica.Usuario usuarios "
												+ "WHERE listas.id = likes.lista AND listas.usuario = usuarios.id AND listas.tipo = 'podcast' AND listas.nombre LIKE ? "
												+ "GROUP BY likes.lista ORDER BY sumaLikes DESC)"
												+ " UNION "
												+ "(SELECT @sumaLikes=0, listas.id id, usuarios.nombre nomUser, listas.nombre nombre, listas.descripcion descripcion, listas.imagen imagen, listas.tipo tipo "
												+ "FROM Reproductor_musica.ListasRep listas, Reproductor_musica.Usuario usuarios "
												+ "WHERE listas.usuario = usuarios.id AND listas.tipo = 'podcast' AND listas.nombre LIKE ? "
												+ "AND listas.id NOT IN (SELECT lista FROM Reproductor_musica.LikesLista))";
	
	private final static String SEARCH_TRANSMISIONES_QUERY = "(SELECT COUNT(*) sumaLikes, trans.id id, trans.nombre nombre, trans.descripcion descripcion, trans.activa activa, trans.usuario idUser, estaciones.url url "
															+ "FROM Reproductor_musica.TransmisionVivo trans, Reproductor_musica.LikesTrans likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Estacion estaciones "
															+ "WHERE trans.id = likes.transmision AND trans.usuario = usuarios.id AND trans.estacion = estaciones.id AND trans.nombre LIKE ? "
															+ "GROUP BY likes.transmision ORDER BY sumaLikes DESC)"
															+ " UNION "
															+ "(SELECT @sumaLikes=0, trans.id id, trans.nombre nombre, trans.descripcion descripcion, trans.activa activa, trans.usuario idUser, estaciones.url url "
															+ "FROM Reproductor_musica.TransmisionVivo trans, Reproductor_musica.Usuario usuarios, Reproductor_musica.Estacion estaciones "
															+ "WHERE trans.usuario = usuarios.id AND trans.estacion = estaciones.id AND trans.nombre LIKE ? "
															+ "AND trans.id NOT IN (SELECT transmision FROM Reproductor_musica.LikesTrans))"
															+ " LIMIT 5";
	private final static String SEARCH_TRANSMISIONES = "(SELECT COUNT(*) sumaLikes, trans.id id, trans.nombre nombre, trans.descripcion descripcion, trans.activa activa, trans.usuario idUser, estaciones.url url "
													+ "FROM Reproductor_musica.TransmisionVivo trans, Reproductor_musica.LikesTrans likes, Reproductor_musica.Usuario usuarios, Reproductor_musica.Estacion estaciones "
													+ "WHERE trans.id = likes.transmision AND trans.usuario = usuarios.id AND trans.estacion = estaciones.id AND trans.nombre LIKE ? "
													+ "GROUP BY likes.transmision ORDER BY sumaLikes DESC)"
													+ " UNION "
													+ "(SELECT @sumaLikes=0, trans.id id, trans.nombre nombre, trans.descripcion descripcion, trans.activa activa, trans.usuario idUser, estaciones.url url "
													+ "FROM Reproductor_musica.TransmisionVivo trans, Reproductor_musica.Usuario usuarios, Reproductor_musica.Estacion estaciones "
													+ "WHERE trans.usuario = usuarios.id AND trans.estacion = estaciones.id AND trans.nombre LIKE ? "
													+ "AND trans.id NOT IN (SELECT transmision FROM Reproductor_musica.LikesTrans))";
	
	private final static String SEARCH_USUARIOS_QUERY = "(SELECT COUNT(*) sumaLikes, users.id id, users.nombre nombre, users.descripcion descripcion, users.mail mail, users.imagen imagen "
														+ "FROM Reproductor_musica.Usuario users, Reproductor_musica.Sigue sigue "
														+ "WHERE users.id = sigue.usuario2 AND users.nombre LIKE ? "
														+ "GROUP BY sigue.usuario2 ORDER BY sumaLikes DESC)"
														+ " UNION "
														+ "(SELECT @sumaLikes=0, users.id id, users.nombre nombre, users.descripcion descripcion, users.mail mail, users.imagen imagen "
														+ "FROM Reproductor_musica.Usuario users "
														+ "WHERE users.nombre LIKE ? "
														+ "AND users.id NOT IN (SELECT usuario2 FROM Reproductor_musica.Sigue))"
														+ " LIMIT 5 ";
	private final static String SEARCH_USUARIOS = "(SELECT COUNT(*) sumaLikes, users.id id, users.nombre nombre, users.descripcion descripcion, users.mail mail, users.imagen imagen "
												+ "FROM Reproductor_musica.Usuario users, Reproductor_musica.Sigue sigue "
												+ "WHERE users.id = sigue.usuario2 AND users.nombre LIKE ? "
												+ "GROUP BY sigue.usuario2 ORDER BY sumaLikes DESC)"
												+ " UNION "
												+ "(SELECT @sumaLikes=0, users.id id, users.nombre nombre, users.descripcion descripcion, users.mail mail, users.imagen imagen "
												+ "FROM Reproductor_musica.Usuario users "
												+ "WHERE users.nombre LIKE ? "
												+ "AND users.id NOT IN (SELECT usuario2 FROM Reproductor_musica.Sigue))";
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una canci�n
	 * Funcionalidad: Completa la lista de datos tipo Audio (id,url,titulo cancion,nombre del autor,nombre del genero al que pertenece)	
	 */
	public static void searchCanciones(String nombre,List<Audio> audios,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_CANCIONES_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Audio result = new Audio(rs.getString("id"), rs.getString("url"), 
						rs.getString("titulo"), rs.getString("nomUser"), rs.getString("nomGenero"));
                audios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una canci�n
	 * Funcionalidad: Completa la lista de datos tipo Audio (id,url,titulo cancion,nombre del autor,nombre del genero al que pertenece)	
	 */
	public static void searchCancionesCompletas(String nombre,List<Audio> audios) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_CANCIONES);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Audio result = new Audio(rs.getString("id"), rs.getString("url"), 
						rs.getString("titulo"), rs.getString("nomUser"), rs.getString("nomGenero"));
                audios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de un cap�tulo Podcast
	 * Funcionalidad: Completa la lista de datos tipo Audio (id,url,titulo capitulo,nombre del autor,nombre del genero al que pertenece)	
	 */
	public static void searchCapitulos(String nombre,List<Audio> audios,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_CAPITULOS_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Audio result = new Audio(rs.getString("id"), rs.getString("url"), 
						rs.getString("titulo"), rs.getString("nomUser"), rs.getString("nomGenero"));
                audios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de un cap�tulo Podcast
	 * Funcionalidad: Completa la lista de datos tipo Audio (id,url,titulo capitulo,nombre del autor,nombre del genero al que pertenece)	
	 */
	public static void searchCapitulosCompletas(String nombre,List<Audio> audios) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_CAPITULOS);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Audio result = new Audio(rs.getString("id"), rs.getString("url"), 
						rs.getString("titulo"), rs.getString("nomUser"), rs.getString("nomGenero"));
                audios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una Lista de reproducci�n
	 * Funcionalidad: Completa la lista de datos tipo ListaReproduccion (id,nombre del creador,titulo,descripcion,URL de la imagen,tipo de lista)	
	 */
	public static void searchListas(String nombre,List<ListaReproduccion> listas,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_LISTASREP_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("nomUser"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                listas.add(result);
			}
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una Lista de reproducci�n
	 * Funcionalidad: Completa la lista de datos tipo ListaReproduccion (id,nombre del creador,titulo,descripcion,URL de la imagen,tipo de lista)	
	 */
	public static void searchListasCompletas(String nombre,List<ListaReproduccion> listas) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_LISTAREP);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("nomUser"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                listas.add(result);
			}
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de un Podcast
	 * Funcionalidad: Completa la lista de datos tipo ListaReproduccion (id,nombre del creador,titulo,descripcion,URL de la imagen,tipo de lista)	
	 */
	public static void searchPodcasts(String nombre,List<ListaReproduccion> podcasts,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_PODCASTS_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("nomUser"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                podcasts.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de un Podcast
	 * Funcionalidad: Completa la lista de datos tipo ListaReproduccion (id,nombre del creador,titulo,descripcion,URL de la imagen,tipo de lista)	
	 */
	public static void searchPodcastsCompletas(String nombre,List<ListaReproduccion> podcasts) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_PODCASTS);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				ListaReproduccion result = new ListaReproduccion(rs.getString("id"), rs.getString("nombre"), 
						rs.getString("nomUser"), rs.getString("descripcion"), rs.getString("imagen"), rs.getString("tipo"));
                podcasts.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
    
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una Transmisi�n en Vivo
	 * Funcionalidad: Completa la lista de datos tipo Transmision (id,titulo,descripcion,booleano para indicar si esta activa,id del creador,URL de la estacion)	
	 */
	public static void searchTransmisiones(String nombre,List<Transmision> transmisiones,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_TRANSMISIONES_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Transmision result = new Transmision(rs.getInt("id"), rs.getString("nombre"), 
						rs.getString("descripcion"), rs.getBoolean("activa"), rs.getInt("idUser"), rs.getString("url"));
				transmisiones.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/*
	 * Parametros: nombre o parte del nombre del t�tulo de una Transmisi�n en Vivo
	 * Funcionalidad: Completa la lista de datos tipo Transmision (id,titulo,descripcion,booleano para indicar si esta activa,id del creador,URL de la estacion)	
	 */
	public static void searchTransmisionesCompletas(String nombre,List<Transmision> transmisiones) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_TRANSMISIONES);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Transmision result = new Transmision(rs.getInt("id"), rs.getString("nombre"), 
						rs.getString("descripcion"), rs.getBoolean("activa"), rs.getInt("idUser"), rs.getString("url"));
				transmisiones.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	/*
	 * Parametros: nombre o parte del nombre de un usuario
	 * Funcionalidad: Completa la lista de datos tipo Usuario (nombre,descripcion,eMail,id,bytes de la imagen)	
	 */
	public static void searchUsuarios(String nombre,List<Usuario> usuarios,Connection conn) {
		try {

			PreparedStatement ps = conn.prepareStatement(SEARCH_USUARIOS_QUERY);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Usuario result = new Usuario(rs.getString("nombre"), rs.getString("descripcion"), 
										rs.getString("mail"), rs.getString("id"), rs.getString("imagen"));
				
				String id = rs.getString("id");
				if (!id.equals("100")) usuarios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	public static void searchUsuariosCompletos(String nombre,List<Usuario> usuarios) {
		try {
			Connection conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(SEARCH_USUARIOS);
            
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				Usuario result = new Usuario(rs.getString("nombre"), rs.getString("descripcion"), 
						rs.getString("mail"), rs.getString("id"), rs.getString("imagen"));
				
				String id = rs.getString("id");
				if (!id.equals("100")) usuarios.add(result);
			}
						
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	/*
	 * Parametros: nombre o parte de un nombre, lista de canciones, lista de capitulos, lista de listas,
	 * 			   lista de podcasts, lista de transmisiones, lista de usuarios (todas vac�as con valor null)
	 * Funcionalidad: Asigna a las listas vac�as los resultados de buscar por nombre
	 */
	public static void searchAll(String nombre, List<Audio> canciones, List<Audio> capitulos, 
					List<ListaReproduccion> listas, List<ListaReproduccion> podcasts,
					List<Transmision> transmisiones, List<Usuario> usuarios) {
		try {
			Connection conn = ConnectionManager.getConnection();
			
			searchCanciones(nombre,canciones,conn);
			searchCapitulos(nombre,capitulos,conn);
			searchListas(nombre,listas,conn);
			searchPodcasts(nombre,podcasts,conn);
			searchTransmisiones(nombre,transmisiones,conn);
			searchUsuarios(nombre,usuarios,conn);
					
			ConnectionManager.releaseConnection(conn);
		} catch(Exception e) {
			e.printStackTrace(System.err);
		}
	
	}
	
    // Prubas con la base de datos
 	//public static void main(String[] args) throws SQLException, IOException{
 		/*
 		List<Audio> canciones = new ArrayList<Audio>();
 		List<Audio> capitulos = new ArrayList<Audio>();
		List<ListaReproduccion> listas = new ArrayList<ListaReproduccion>();
		List<ListaReproduccion> podcasts = new ArrayList<ListaReproduccion>();
		List<Transmision> transmisiones = new ArrayList<Transmision>();
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		searchAll("a",canciones,capitulos,listas,podcasts,transmisiones,usuarios);
		
		try {
			
	 		for (Audio a : canciones) System.out.println(a.getTitulo());
	 		
	 		System.out.println("----------CAPS------------");
	 		
	 		for (Audio a : capitulos) System.out.println(a.getTitulo());
	 		
	 		System.out.println("-----------LISTS-----------");
	 		
	 		for (ListaReproduccion l : listas) System.out.println(l.getNombre());
	 		
	 		System.out.println("-----------PODS-----------");
	 		
	 		for (ListaReproduccion l : podcasts) System.out.println(l.getNombre());
	 		
	 		System.out.println("----------TRANS------------");
	 		
	 		for (Transmision t : transmisiones) System.out.println(t.getNombre());
	 		
	 		System.out.println("-----------USRS-----------");
	 		
	 		for (Usuario u : usuarios) System.out.println(u.getNombre());
	 		
		}catch(Exception e){
			e.printStackTrace(System.err);
		}
 		*/
 	//}
}