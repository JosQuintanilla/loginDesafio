package com.accenture.login.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "created")
	private Date created;

	@Column(name = "modified")
	private Date modified;

	@Column(name = "last_login")
	private Date last_login;

	@Column(name = "token")
	private String token;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuario")
	private List<Telefono> phones;

	public Usuario() {
	}

	public Usuario(String id, Date created, Date modified, Date last_login, String token, String name, String email, String password) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.last_login = last_login;
		this.token = token;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Usuario(String id, Date created, Date modified, Date last_login, String token, String name, String email, String password, List<Telefono> phones) {
		super();
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.last_login = last_login;
		this.token = token;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phones = phones;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getLast_login() {
		return last_login;
	}

	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public List<Telefono> getPhones() {
		return phones;
	}

	public void setPhones(List<Telefono> phones) {
		this.phones = phones;
	}

}