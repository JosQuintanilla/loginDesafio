package com.accenture.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "telefono", uniqueConstraints = @UniqueConstraint(columnNames = {"number", "id"})) //, )
public class Telefono {

	@Id
	@Column(name = "number")
	private Integer number;

	@Column(name = "citycode")
	private Integer citycode;

	@Column(name = "contrycode")
	private Integer contrycode;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	private Usuario usuario;

	public Telefono() {
	}

	public Telefono(Integer number, Integer citycode, Integer contrycode) {
		super();
		this.number = number;
		this.citycode = citycode;
		this.contrycode = contrycode;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getCitycode() {
		return citycode;
	}

	public void setCitycode(Integer citycode) {
		this.citycode = citycode;
	}

	public Integer getContrycode() {
		return contrycode;
	}

	public void setContrycode(Integer contrycode) {
		this.contrycode = contrycode;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
}