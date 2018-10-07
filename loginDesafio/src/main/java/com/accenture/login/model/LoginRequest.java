package com.accenture.login.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginRequest {

	@NotNull(message = "Debe ingresar un correo")
	@NotBlank(message = "Debe ingresar un correo")
	@Email(message = "Correo sin formato")
	private String correo;
	
	@NotNull(message = "Debe ingresar una contraseña")
	@NotBlank(message = "Debe ingresar una contraseña")
	@Size(min = 6, max = 12, message = "la contraseña debe ser de largo minimo 6 y maximo 12 caracteres")
	private String contraseña;

	public LoginRequest() {
		super();
	}

	public LoginRequest(String correo, String contraseña) {
		super();
		this.correo = correo;
		this.contraseña = contraseña;
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

}