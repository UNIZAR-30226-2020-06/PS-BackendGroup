package com.espotify.model;

import com.espotify.dao.CancionDAO;

public class CapituloPodcast {
	private int id;
	private int id_genero;
	private int id_autor;
	private String titulo;
	private String url;
	
	public CapituloPodcast() {
		
	}
	
	public CapituloPodcast(int id, int id_genero, int id_autor, String titulo, String url) {
		super();
		this.id = id;
		this.id_autor = id_autor;
		this.id_genero = id_genero;
		this.titulo = titulo;
		this.url = url;
	}
	
	public int getId() {
		return id;
	}
	
	public int getIDGenero() {
		return id_genero;
	}
	
	public int getIDAutor() {
		return id_autor;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public String getURL() {
		return url;
	}
	
	public String getNombreAutor() {
		CancionDAO cancion = new CancionDAO();
		return cancion.obtenerNombreAutor(id_autor);
	}
	
	public String getNombreGenero() {
		CancionDAO cancion = new CancionDAO();
		return cancion.obtenerNombreGenero(id_genero);
	}
}