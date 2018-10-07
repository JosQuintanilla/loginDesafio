package com.accenture.login.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.login.constant.Constantes;
import com.accenture.login.converter.UsuarioConverter;
import com.accenture.login.model.LoginRequest;
import com.accenture.login.model.ResponseJSON;
import com.accenture.login.model.Usuario;
import com.accenture.login.model.UsuarioRequest;
import com.accenture.login.repository.UsuarioRepositoryImp;
import com.accenture.login.utils.Utils;

@RestController
@RequestMapping("/api")
public class LoginController extends Constantes {

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

	// Rggistar un usuario
	@PostMapping("/registrarUsuario")
	public ResponseJSON registrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult) {
		logger.info("registrarUsuario init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			if (usuarioRepository.existeUsuario(usuarioRequest.getCorreo())) {
				logger.info("registrarUsuario " + Constantes.CORREO_REGISTRADO);
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				List<String> listaError = new ArrayList<String>();
				listaError.add("mensaje : " + Constantes.CORREO_REGISTRADO);
				resJSON.setPayload(listaError);
			} else {
				Usuario usuario = new Usuario();
				usuario = usuarioConverter.resquestToModel(usuarioRequest);
				usuario.setId(UUID.randomUUID().toString());
				usuario.setCreated(new Date());
				usuario.setModified(new Date());
				usuario.setLast_login(new Date());
				usuario.setToken(utils.createToken(usuario.getId()));
				///
				logger.info("registrarUsuario usuario ID: " + usuario.getId());
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				resJSON.setPayload(usuarioRepository.regitrarUsaurio(usuario));
			}
		}
		return resJSON;
	}

	// Eliminar un Uusario
	@DeleteMapping("/deleteUsuario/{email}")
	public ResponseJSON eliminarUsuario(@PathVariable("email") String email) {
		logger.info("eliminarUsuario init");
		logger.info("eliminarUsuario email: " + email);
		if (utils.validarEmail(email)) {
			List<String> listaMensajes = new ArrayList<String>();
			resJSON.setDescripcion("OK");
			resJSON.setStatus(200);
			if (usuarioRepository.eliminarUsuario(email)) {
				listaMensajes.add("mensaje : Usuario Eliminado");
			} else {
				listaMensajes.add("mensaje : Error al eliminar el usuario de correo: " + email);
			}
			resJSON.setPayload(listaMensajes);
		} else {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			List<String> listaError = new ArrayList<String>();
			listaError.add("mensaje : " + Constantes.CORREO_SIN_FORMATO);
			resJSON.setPayload(listaError);
		}
		return resJSON;
	}

	// Listar Usuarios
	@GetMapping("/verUsuarios")
	public ResponseJSON listarUsuarios() {
		logger.info("listarUsuarios init");
		resJSON.setDescripcion("OK");
		resJSON.setStatus(200);
		resJSON.setPayload(usuarioRepository.listarUsuarios());
		return resJSON;
	}

	/////////// LOGIN////////////
	// Rggistar un usuario
	@PostMapping("/login")
	public ResponseJSON login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
		logger.info("registrarUsuario init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			Usuario usuario = new Usuario();
			usuario =usuarioRepository.login(loginRequest);
			if(usuario.getId() != null && usuario.getId() != "") {
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				resJSON.setPayload(usuario);
			}else {
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(401);
				List<String> listaError = new ArrayList<String>();
				listaError.add("mensaje : " + Constantes.NOT_LOGIN);
				resJSON.setPayload(listaError);
			}			
		}
		return resJSON;
	}

}