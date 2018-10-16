package com.accenture.login.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import com.accenture.login.service.UsuarioService;
import com.accenture.login.utils.Utils;

@RestController
@RequestMapping("/api")
public class LoginController extends Constantes {

	final static Logger logger = Logger.getLogger(LoginController.class);
	private final ResponseJSON resJSON = new ResponseJSON();
	
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioService usuarioService;
	
	@Autowired
	@Qualifier("usuarioConverter")
	private UsuarioConverter usuarioConverter;

	@Autowired
	@Qualifier("utils")
	private Utils utils;

	// SIN VALIDACION DE HEADER
	// Registar un usuario
	@PostMapping("/registrarUsuario")
	public ResponseJSON registrarUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult) {
		logger.info("registrarUsuario init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			if (usuarioService.existeUsuarioByEmail(usuarioRequest.getCorreo())) {
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.CORREO_REGISTRADO);
				resJSON.setPayload(mapaMensajes);
			} else {
				usuarioRequest.setId(UUID.randomUUID().toString());
				//Se encripta la password
				//usuarioRequest.setContraseña(utils.encriptarPass(usuarioRequest.getContraseña()));
				logger.info("registrarUsuario usuarioRequest Password Original: "+usuarioRequest.getContraseña());
				usuarioRequest.setContraseña(BCrypt.hashpw(usuarioRequest.getContraseña(), BCrypt.gensalt()));
				logger.info("registrarUsuario usuarioRequest Password Cryp: "+usuarioRequest.getContraseña());
				Usuario usuario = new Usuario();
				usuario = usuarioConverter.resquestToEntity(usuarioRequest);
				logger.info("registrarUsuario usuario Password Cryp: "+usuario.getPassword());
				usuario.setCreated(new Date());
				usuario.setModified(new Date());
				usuario.setLast_login(new Date());
				usuario.setToken(utils.createToken(usuario.getEmail()));
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				resJSON.setPayload(usuarioConverter.entityToResponse(usuarioService.registarUsuario(usuario)));
			}
		}
		return resJSON;
	}

	// CON VALIDACION DE TOKEN en HEADER
	// Listar Usuarios
	@GetMapping("/verUsuarios/{email}")
	public ResponseJSON listarUsuarios(@PathVariable("email") String email, @RequestHeader(value = "token") String token) {
		logger.info("verUsuarios init");
		if (utils.isTokenValid(token)) {
			logger.info("listarUsuarios token valido");
			resJSON.setDescripcion("OK");
			resJSON.setStatus(200);
			resJSON.setPayload(usuarioService.verUsuarios());
		} else {
			logger.info("listarUsuarios token invalido");
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			Map<String, String> mapaMensajes = new HashMap<>();
			mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
			resJSON.setPayload(mapaMensajes);
		}
		return resJSON;
	}

	/////////// LOGIN////////////
	@PostMapping("/login")
	public ResponseJSON login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult, @RequestHeader(value = "token") String token) {
		logger.info("Login init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(401);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		} else {
			//Valido el Token
			if (utils.isTokenValid(token)) {
				/*
				 * loginRequest.setContraseña(utils.encriptarPass(loginRequest.getContraseña()))
				 * ; logger.info("Login Password Encriptarada: " +loginRequest.getContraseña());
				 */
				Usuario usuario = usuarioService.login(loginRequest);
				if (usuario != null && usuario.getEmail() != null && usuario.getEmail() != "") {
					//Actualizo fecha de Logeo
					usuario.setLast_login(new Date());
					usuarioService.actualizarUsuario(usuario);				
					resJSON.setDescripcion("OK");
					resJSON.setStatus(200);
					resJSON.setPayload(usuarioConverter.entityToResponse(usuario));
				} else {
					resJSON.setDescripcion("ERROR");
					resJSON.setStatus(401);
					Map<String, String> mapaMensajes = new HashMap<>();
					mapaMensajes.put("mensaje", Constantes.NOT_LOGIN);
					resJSON.setPayload(mapaMensajes);
				}
			} else {
				logger.info("Login token invalido");
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
				resJSON.setPayload(mapaMensajes);
			}
		}
		return resJSON;
	}

	// Eliminar un Uusario
	@DeleteMapping("/deleteUsuario/{email}")
	public ResponseJSON eliminarUsuario(@PathVariable("email") String email, @RequestHeader(value = "token") String token) {
		logger.info("eliminarUsuario init");
		logger.info("eliminarUsuario email: " + email);
		if (utils.isTokenValid(token)) {
			logger.info("eliminarUsuario Token Valido");
			if (utils.validarEmail(email)) {
				logger.info("eliminarUsuario Email Valido");
				//Busco el usaurio por email
				Usuario usuario = usuarioService.buscarUsuarioByEmail(email);
				Map<String, String> mapaMensajes = new HashMap<>();
				if(usuarioService.eliminarUSuario(usuario)) {
					resJSON.setDescripcion("OK");
					resJSON.setStatus(200);
					mapaMensajes.put("mensaje", "Usuario Eliminado");
				}else {
					resJSON.setDescripcion("ERROR");
					resJSON.setStatus(400);
					mapaMensajes.put("mensaje", "Error al eliminar el usuario de correo: " + email);
				}				
			} else {
				logger.info("eliminarUsuario Email invalido");
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.CORREO_SIN_FORMATO);
				resJSON.setPayload(mapaMensajes);
			}			
		} else {
			logger.info("eliminarUsuario token invalido");
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			Map<String, String> mapaMensajes = new HashMap<>();
			mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
			resJSON.setPayload(mapaMensajes);
		}
		return resJSON;
	}

	// Modificar un Usario
	@PostMapping("/modificarUsuario")
	public ResponseJSON modificarUSuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult, @RequestHeader(value = "token") String token) {
		logger.info("modificarUSuario init");
		if (bindingResult.hasErrors()) {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			resJSON.setPayload(utils.obtenerMsjValidacion(bindingResult.getAllErrors()));
		}else {
			if (utils.isTokenValid(token)) {	
				logger.info("modificarUSuario token valido");
				//Obtengo el usuario desde Base
				Usuario usuario = usuarioService.buscarUsuarioByEmail(usuarioRequest.getCorreo());
				//Mapeo los campos nuevos 
				usuario.setName(usuarioRequest.getNombre());
				usuario.setPassword(usuarioRequest.getContraseña());
				usuario.setModified(new Date());
				usuario.setPhones(usuarioConverter.teleFonoModelToEntity(usuarioRequest.getListaTelefonos(), usuario));
				logger.info("modificarUSuario antes de actulizar");
				usuarioService.actualizarUsuario(usuario);
				logger.info("modificarUSuario despues de actulizar");
				resJSON.setDescripcion("OK");
				resJSON.setStatus(200);
				//if (usuarioRepository.eliminarUsuario("")) {
				//	mapaMensajes.put("mensaje", "Usuario Eliminado");
				//} else {
				//	mapaMensajes.put("mensaje", "Error al eliminar el usuario de correo: " + "");
				//}
				resJSON.setPayload(usuarioConverter.entityToResponse(usuario));
			} else {
				logger.info("modificarUSuario token invalido");
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
				resJSON.setPayload(mapaMensajes);
			}
		}
		return resJSON;
	}

}