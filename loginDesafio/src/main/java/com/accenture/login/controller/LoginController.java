package com.accenture.login.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.login.constant.Constantes;
import com.accenture.login.converter.UsuarioConverter;
import com.accenture.login.entity.Usuario;
import com.accenture.login.model.LoginRequest;
import com.accenture.login.model.ResponseJSON;
import com.accenture.login.model.UsuarioRequest;
import com.accenture.login.model.UsuarioResponse;
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

	// SIN HEADER
	// Registar un usuario
	@PostMapping("/registrarUsuario")
	public ResponseJSON registrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult) {
		logger.info("registrarUsuario init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			if (usuarioRepository.existeUsuario(usuarioRequest.getCorreo())) {
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.CORREO_REGISTRADO);
				resJSON.setPayload(mapaMensajes);
			} else {
				Usuario usuario = new Usuario();
				usuario = usuarioConverter.resquestToEntity(usuarioRequest);
				logger.info("registrarUsuario Password Original: " + usuario.getPassword());
				usuario.setPassword(utils.encriptarPass(usuario.getPassword()));
				logger.info("registrarUsuario Password Encriptarda: " + usuario.getPassword());
				usuario.setId(UUID.randomUUID().toString());
				usuario.setCreated(new Date());
				usuario.setModified(new Date());
				usuario.setLast_login(new Date());
				usuario.setToken(utils.createToken(usuario.getId()));
				///
				logger.info("registrarUsuario usuario ID: " + usuario.getId());
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				resJSON.setPayload(usuarioConverter.entityToResponse(usuarioRepository.regitrarUsaurio(usuario)));

			}
		}
		return resJSON;
	}

	// CON VALIDACION DE HEADER
	// Listar Usuarios
	@GetMapping("/verUsuarios")
	public ResponseJSON listarUsuarios(@RequestHeader(value="token") String token) {
		logger.info("listarUsuarios init");
		logger.info("listarUsuarios TOKEN :"+token);
		resJSON.setDescripcion("OK");
		resJSON.setStatus(200);
		resJSON.setPayload(usuarioRepository.listarUsuarios());
		return resJSON;
	}

	/////////// LOGIN////////////
	@PostMapping("/login")
	public ResponseJSON login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
		logger.info("Login init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(401);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			UsuarioResponse usuarioResponse = new UsuarioResponse();
			logger.info("Login Password Original: " + loginRequest.getContraseña());
			/*
			 * loginRequest.setContraseña(utils.encriptarPass(loginRequest.getContraseña()))
			 * ; logger.info("Login Password Encriptarada: " +loginRequest.getContraseña());
			 */
			usuarioResponse = usuarioRepository.login(loginRequest);
			if (usuarioResponse.getEmail() != null && usuarioResponse.getEmail() != "") {
				usuarioRepository.actualizarFechaLogin(usuarioResponse.getEmail());
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				resJSON.setPayload(usuarioResponse);
			} else {
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(401);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.NOT_LOGIN);
				resJSON.setPayload(mapaMensajes);
			}
		}
		return resJSON;
	}

	///////////////////
	// Eliminar un Uusario
	@DeleteMapping("/deleteUsuario/{email}")
	public ResponseJSON eliminarUsuario(@PathVariable("email") String email) {
		logger.info("eliminarUsuario init");
		logger.info("eliminarUsuario email: " + email);
		if (utils.validarEmail(email)) {
			Map<String, String> mapaMensajes = new HashMap<>();
			resJSON.setDescripcion("OK");
			resJSON.setStatus(200);
			if (usuarioRepository.eliminarUsuario(email)) {
				mapaMensajes.put("mensaje", "Usuario Eliminado");
			} else {
				mapaMensajes.put("mensaje", "Error al eliminar el usuario de correo: " + email);
			}
			resJSON.setPayload(mapaMensajes);
		} else {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			Map<String, String> mapaMensajes = new HashMap<>();
			mapaMensajes.put("mensaje", Constantes.CORREO_SIN_FORMATO);
			resJSON.setPayload(mapaMensajes);
		}
		return resJSON;
	}

}