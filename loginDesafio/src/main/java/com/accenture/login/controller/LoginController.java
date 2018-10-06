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

import com.accenture.login.model.Usuario;
import com.accenture.login.repository.UsuarioRepositoryImp;

@RestController
@RequestMapping("/api")
public class LoginController {

	final static Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	@Qualifier("usuarioRepository")
	private UsuarioRepositoryImp usuarioRepository;

	// Retornar un usuario seleccionado
	@PostMapping("/registrarUsuario")
	public Usuario registrarUsuario(@RequestBody Usuario usuario) {
		logger.info("LoginController - registrarUsuario init"); 
		System.out.println("LoginController - registrarUsuario init");
		System.out.println("LoginController - registrarUsuario usuario ID: "+usuario.getId());
		usuarioRepository.save(usuario);
		return usuario;
	}

	// Listar Usuarios
	@GetMapping("/verUsuarios")
	public List<Usuario> listarUsuarios() {
		logger.info("listarUsuarios init");
		System.out.println("LoginController - listarUsuarios init");
		return usuarioRepository.listarUsuarios();
	}

}