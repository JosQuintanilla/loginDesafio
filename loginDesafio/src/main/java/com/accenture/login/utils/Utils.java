package com.accenture.login.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import com.accenture.login.constant.Constantes;
import com.accenture.login.entity.Usuario;
import com.accenture.login.repository.UsuarioRepositoryImp;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component("utils")
public class Utils extends Constantes {
	
	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepositoryImp usuarioRepository;

	public List<String> obtenerMsjValidacion(List<ObjectError> listErrores) {
		List<String> listaErrores = new ArrayList<>();
		for (ObjectError error : listErrores) {
			listaErrores.add("mensaje: " + error.getDefaultMessage());
		}
		return listaErrores;
	}

	public String createToken(String email) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			Date fechaHasta = new Date();
			fechaHasta.setMinutes(fechaHasta.getMinutes() +TIEMPOTOKEN);
			String token = JWT.create().withClaim("email", email).withExpiresAt(fechaHasta).withClaim("fechaCreacion", new Date()).sign(algorithm);
			return token;
		} catch (UnsupportedEncodingException exception) {
			exception.printStackTrace();
			// log WRONG Encoding message
		} catch (JWTCreationException exception) {
			exception.printStackTrace();
			// log Token Signing Failed
		}
		return null;
	}

	public String getEmailFromToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT jwt = verifier.verify(token);
			System.out.println("Email: "+jwt.getClaim("email").asString());
			Date expiresAt = jwt.getExpiresAt();
			System.out.println("Fecha Exp: "+expiresAt);
			return jwt.getClaim("email").asString();
		} catch (UnsupportedEncodingException exception) {
			exception.printStackTrace();
			// log WRONG Encoding message
			return null;
		} catch (JWTVerificationException exception) {
			exception.printStackTrace();
			// log Token Verification Failed
			return null;
		}
	}

	public boolean isTokenValid(String token) {
		String email = this.getEmailFromToken(token);
		//Valido que sea el mismo token que esta en base de datos		
		Usuario usuario = new Usuario();
		usuario = usuarioRepository.obtenerToken(email);
		if(usuario != null && usuario.getToken() != null && usuario.getToken() != "" ) {
			if(usuario.getToken().equals(token)) {
				return email != null;
			}else {
				return false;
			}
		}else {
			return false;
		}				
	}

	public boolean validarEmail(String email) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher mather = pattern.matcher(email);
		if (mather.find() == true) {
			System.out.println("El email ingresado es válido.");
			return true;
		} else {
			System.out.println("El email ingresado es inválido.");
			return false;
		}
	}
	
	public String encriptarPass(String password) {
		BCryptPasswordEncoder be = new BCryptPasswordEncoder();
		return be.encode(password);
	}
	
}