package com.accenture.login.model;

public class ResponseJSON {

	private Integer status;
	private String descripcion;
	private Object payload;

	public ResponseJSON() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponseJSON(Integer status, String descripcion, Object payload) {
		super();
		this.status = status;
		this.descripcion = descripcion;
		this.payload = payload;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}