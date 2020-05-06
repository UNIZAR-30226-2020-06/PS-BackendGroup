package com.espotify.model;

import com.mysql.cj.jdbc.Blob;

/**
 * Clase que define la estructura de una lista de reproducción.
 */

public class ListaReproduccion {
	
    private String id;
	private String usuario;
	private String nombre;
	private String descripcion;
	private Blob imagen;
	private String tipo;
	
	public ListaReproduccion(String id, String nombre, String usuario, String descripcion, Blob imagen, String tipo) {
		super();
        this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.tipo = tipo;
		this.usuario = usuario;
	}
	
    public String getId() {
        return id;
    }
    
	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Blob getImagen() {
		return imagen;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String getUsuario() {
		return usuario;
	}
}
