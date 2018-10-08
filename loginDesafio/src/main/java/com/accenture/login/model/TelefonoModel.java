package com.accenture.login.model;

public class TelefonoModel {
	
	private Integer number;
	private Integer citycode;
	private Integer contrycode;
	
	public TelefonoModel() {
		super();
	}
	
	public TelefonoModel(Integer number, Integer citycode, Integer contrycode) {
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
}