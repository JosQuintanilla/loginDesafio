package com.accenture.login.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.login.model.ResponseJSON;
import com.accenture.login.model.Usuario;
import com.accenture.login.repository.UsuarioRepositoryImp;

@RestController
@RequestMapping("/api")
public class LoginController {

	final static Logger logger = Logger.getLogger(LoginController.class);
	private final ResponseJSON resJSON = new ResponseJSON();

	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepositoryImp usuarioRepository;

	// Retornar un usuario seleccionado
	@PostMapping("/registrarUsuario")
	public ResponseJSON  registrarUsuario(@RequestBody Usuario usuario) {
		logger.info("registrarUsuario init"); 
		logger.info("registrarUsuario usuario ID: "+usuario.getId());
		resJSON.setDescripcion("ok");
		resJSON.setStatus(200);
		resJSON.setPayload(usuarioRepository.regitrarUsaurio(usuario));
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