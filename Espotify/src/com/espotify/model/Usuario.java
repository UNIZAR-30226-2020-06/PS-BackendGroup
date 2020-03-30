package com.espotify.model;

/**
 * Clase que define la estructura de un usuario.
 */
public class Usuario {
	private String nombre;
	private String descripcion;
	private String correo;
	private String id;
	
	public Usuario(){
		
	}
	
	public Usuario(String nombre, String descripcion, String correo, String id) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.correo = correo;
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getCorreo() {
		return correo;
	}
	
	public String getId() {
		return id;
	}
}