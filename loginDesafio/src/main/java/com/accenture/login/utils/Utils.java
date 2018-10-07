package com.accenture.login.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.accenture.login.constant.Constantes;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component("utils")
public class Utils extends Constantes{
	
	public List<String> obtenerMsjValidacion (List<ObjectError> listErrores){
		List<String> listaErrores = new ArrayList<>();
		for (ObjectError error : listErrores) {
			listaErrores.add("mensaje: "+error.getDefaultMessage());
		}			
		return listaErrores;
	}
	
	public String createToken(String userid) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
           
            String token = JWT.create()
                    .withClaim("userId",userid)
                    .withClaim("createdAt", new Date())
                    .sign(algorithm);
            return token;
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
            //log WRONG Encoding message
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
            //log Token Signing Failed
        }
        return null;
    }
	
	public String getUserIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userId").asString();
        } catch (UnsupportedEncodingException exception) {
            exception.printStackTrace();
            //log WRONG Encoding message
            return null;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            //log Token Verification Failed
            return null;
        }
    }
	
	public boolean isTokenValid(String token) {
        String userId = this.getUserIdFromToken(token);
        return userId != null;
    }
	
	/**
	 * 

	 
	
	public String encriptar(String texto) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(Constantes.PASSWORD_JASYPT);                     
		encryptor.setAlgorithm(Constantes.ALGORITHM_JASYPT);    
		return encryptor.encrypt(texto);
	}
	
	public String desencriptar(String texto) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(Constantes.PASSWORD_JASYPT);                     
		encryptor.setAlgorithm(Constantes.ALGORITHM_JASYPT);  
		return encryptor.decrypt(texto);
	}
	
	public boolean validarEmail (String email) {
		// Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            System.out.println("El email ingresado es válido.");
            return true;
        } else {
            System.out.println("El email ingresado es inválido.");
            return false;
        }		
	}*/
}