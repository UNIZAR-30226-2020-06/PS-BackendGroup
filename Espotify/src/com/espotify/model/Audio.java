package com.espotify.model;

/**
 * Clase que define la estructura de un audio.
 */

public class Audio {
	
    private String id;
	private String url;
	private String titulo;
	private String usuario;
	private String genero;
	
	public Audio(String id, String url, String titulo, String usuario, String genero) {
		super();
        this.id = id;
		this.url = url;
		this.titulo = titulo;
		this.usuario = usuario;
		this.genero = genero;
	}
	
    public String getId() {
        return id;
    }
	
	public String getUrl() {
        return url;
	}

	public String getTitulo() {
        return titulo;
    }
	
	public String getUsuario() {
		return usuario;
	}

	public String getGenero() {
        return genero;
    }
}
