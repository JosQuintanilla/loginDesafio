package com.accenture.login.model;

import java.util.List;

public class UsuarioResponse {

	private String name;
	private String email;
	private String password;
	private String token;
	private List<TelefonoModel> phones;

	public UsuarioResponse() {
		super();
	}

	public UsuarioResponse(String name, String email, String password, List<TelefonoModel> phones) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<TelefonoModel> getPhones() {
		return phones;
	}

	public void setPhones(List<TelefonoModel> phones) {
		this.phones = phones;
	}

}