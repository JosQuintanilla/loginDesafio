package com.accenture.login.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.accenture.login.converter.UsuarioConverter;
import com.accenture.login.entity.Telefono;
import com.accenture.login.entity.Usuario;
import com.accenture.login.model.LoginRequest;
import com.accenture.login.model.UsuarioResponse;
import com.accenture.login.repository.TelefonoRepositoryJPA;
import com.accenture.login.repository.UserRepositoryJPA;

@Service("usuarioService")
public class UsuarioService {
	
	private final static Logger logger = Logger.getLogger(UsuarioService.class);

	@Autowired
	@Qualifier("userRepositoryJPA")
	private UserRepositoryJPA userRepositoryJPA;
	
	@Autowired
	@Qualifier("telefonoRepositoryJPA")
	private TelefonoRepositoryJPA telefonoRepositoryJPA;
	
	@Autowired
	@Qualifier("usuarioConverter")
	private UsuarioConverter usuarioConverter;	
	
	public List<UsuarioResponse> verUsuarios(){
		logger.info("verUsuarios - init");
		return usuarioConverter.listaUsuariosToListUsuarioResponse(userRepositoryJPA.findAll());
	}
	
	public boolean existeUsuarioByEmail(String email) {
		logger.info("findUsuarioByEmail - init");
		Boolean existeEmail = false;
		Usuario usuario = new Usuario();
		usuario = userRepositoryJPA.findUsuarioByEmail(email);
		if(usuario != null && usuario.getEmail() != null && usuario.getEmail() != "") {
			existeEmail = true;
		}
		return existeEmail;
	}
	
	public Usuario buscarUsuarioByEmail(String email) {
		logger.info("findUsuarioByEmail - init");
		return userRepositoryJPA.findUsuarioByEmail(email);
	}
	
	public Usuario registarUsuario(Usuario usuario) {
		logger.info("registarUsuario - init");		
		List<Telefono> listaTelefono = new ArrayList<>(usuario.getPhones());
		usuario.setPhones(new ArrayList<Telefono>());	
		userRepositoryJPA.save(usuario);
		for (Telefono telefono : listaTelefono) {
			telefonoRepositoryJPA.save(telefono);
		}	
		usuario.setPhones(listaTelefono);
		return usuario;
	}
	
	public void actualizarUsuario(Usuario usuario) {
		logger.info("actualizarUsuario - init");
		List<Telefono> listaTelefono = new ArrayList<>(usuario.getPhones());
		usuario.setPhones(new ArrayList<Telefono>());	
		userRepositoryJPA.save(usuario);
		usuario.setPhones(listaTelefono);		
		logger.info("actualizarUsuario - end");	
	}
	
	public Usuario login(LoginRequest loginRequest) {
		logger.info("login - init");
		return userRepositoryJPA.loginUser(loginRequest.getCorreo(), loginRequest.getContraseña());
	}
	
	public Boolean eliminarUSuario(Usuario usuario) {
		List<Telefono> listaTelefono = new ArrayList<>(usuario.getPhones());
		usuario.setPhones(new ArrayList<Telefono>());	
		for(Telefono telefono: listaTelefono) {
			telefonoRepositoryJPA.delete(telefono);
		}
		userRepositoryJPA.delete(usuario);
		return true;
	}
	/**
	
	
	 */

}