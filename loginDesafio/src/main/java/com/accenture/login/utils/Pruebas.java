package com.accenture.login.utils;

public class Pruebas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		
		System.out.println(utils.encriptarPass("123456"));
		
		System.out.println(utils.encriptarPass("654321"));
		
		System.out.println(utils.encriptarPass("123456"));

	}

}
