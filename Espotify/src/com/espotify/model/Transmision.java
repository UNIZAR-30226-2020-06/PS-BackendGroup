package com.espotify.model;

/**
 * Clase que define la estructura de un audio.
 */

public class Transmision {
	
	private int id;
    private String nombre;
	private String descripcion;
	private boolean activa;
	private int usuario;
	private String url;
	
	public Transmision(int id, String nombre, String descripcion, boolean activa, int usuario, String url) {
		super();
        this.id = id;
		this.url = url;
		this.nombre = nombre;
		this.activa = activa;
		this.descripcion = descripcion;
		this.usuario = usuario;
	}
	
    public int getId() {
        return id;
    }
	
	public String getUrl() {
        return url;
	}

	public String getNombre() {
        return nombre;
    }
	
	public boolean getActiva() {
		return activa;
	}
	
	public int getUsuario() {
		return usuario;
	}

	public String getDescripcion() {
        return descripcion;
    }
}
