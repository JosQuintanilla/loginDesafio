package com.accenture.login.utils;

import java.util.Date;

public class Pruebas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		
		Date fechaHasta = new Date();
		
		System.out.println("fecha Normal: "+fechaHasta);
		
		fechaHasta.setMinutes(fechaHasta.getMinutes() +7);
		
		System.out.println("fecha Normal +7: "+fechaHasta);
		System.out.println(utils.encriptarPass("654321"));
		
		System.out.println(utils.encriptarPass("123456"));

	}

}
