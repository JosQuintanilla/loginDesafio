package com.accenture.login.utils;

public class Pruebas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Utils utils = new Utils();
		
		String token = utils.createToken("email@algo.com");
		
		System.out.println("Token: "+token);
		
		System.out.println(utils.isTokenValid(token));
	}
}