package com.accenture.login.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UsuarioRequest {

	@NotNull(message = "Debe ingresar un nombre")
	@NotBlank(message = "Debe ingresar un nombre")
	private String nombre;
	
	@NotNull(message = "Debe ingresar un correo")
	@NotBlank(message = "Debe ingresar un correo")
	@Email(message = "Correo sin formato")
	private String correo;
	
	@NotNull(message = "Debe ingresar una contraseña")
	@NotBlank(message = "Debe ingresar una contraseña")
	@Size(min = 6, max = 12, message = "la contraseña debe ser de largo minimo 6 y maximo 12 caracteres")
	private String contraseña;
	
	private List<TelefonoModel> listaTelefonos;

	public UsuarioRequest() {
	}

	public UsuarioRequest(String nombre, String correo, String contraseña) {
		super();
		this.nombre = nombre;
		this.correo = correo;
		this.contraseña = contraseña;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public List<TelefonoModel> getListaTelefonos() {
		return listaTelefonos;
	}

	public void setListaTelefonos(List<TelefonoModel> listaTelefonos) {
		this.listaTelefonos = listaTelefonos;
	}
}