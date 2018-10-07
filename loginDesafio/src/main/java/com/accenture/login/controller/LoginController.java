package com.accenture.login.controller;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.login.converter.UsuarioConverter;
import com.accenture.login.model.ResponseJSON;
import com.accenture.login.model.Usuario;
import com.accenture.login.model.UsuarioRequest;
import com.accenture.login.repository.UsuarioRepositoryImp;
import com.accenture.login.utils.Utils;

@RestController
@RequestMapping("/api")
public class LoginController {

	final static Logger logger = Logger.getLogger(LoginController.class);
	private final ResponseJSON resJSON = new ResponseJSON();

	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepositoryImp usuarioRepository;
	
	@Autowired
	@Qualifier("usuarioConverter")
	private UsuarioConverter usuarioConverter;
	
	@Autowired
	@Qualifier("utils")
	private Utils utils;

	// Retornar un usuario seleccionado
	@PostMapping("/registrarUsuario")
	public ResponseJSON  registrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult) {
		logger.info("registrarUsuario init"); 
		if (bindingResult.hasErrors()) {
			resJSON.setStatus(400);
			resJSON.setDescripcion("ERROR");
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			Usuario usuario = new Usuario();
			usuario = usuarioConverter.resquestToModel(usuarioRequest);
			usuario.setId(UUID.randomUUID().toString());
			usuario.setCreated(new Date());
			usuario.setModified(new Date());
			usuario.setLast_login(new Date());
			usuario.setToken(utils.createToken(usuario.getId()));			
			///
			logger.info("registrarUsuario usuario ID: "+usuario.getId());
			resJSON.setDescripcion("ok");
			resJSON.setStatus(200);
			resJSON.setPayload(usuarioRepository.regitrarUsaurio(usuario));			
		}
		return resJSON;
	}

	// Listar Usuarios
	@GetMapping("/verUsuarios")
	public ResponseJSON listarUsuarios() {
		logger.info("listarUsuarios init");
		resJSON.setDescripcion("ok");
		resJSON.setStatus(200);
		resJSON.setPayload(usuarioRepository.listarUsuarios());
		return resJSON;
	}

}