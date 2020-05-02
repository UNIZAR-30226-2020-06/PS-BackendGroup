package com.espotify.model;

/**
 * Clase que define la estructura de un audio.
 */

public class Comentario {
	
	private int id;
	private String descripcion;
	private String usuario;

	
	public Comentario(int id, String descripcion, String usuario) {
		super();
        this.id = id;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}
	
    public int getId() {
        return id;
    }

	public String getDescripcion() {
        return descripcion;
    }
	
	public String getUsuario() {
		return usuario;
	}
}
