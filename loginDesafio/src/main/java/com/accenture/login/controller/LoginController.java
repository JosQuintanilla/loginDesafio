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
import com.accenture.login.service.UsuarioService;
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
			if (usuarioService.findUsuarioByEmail(usuarioRequest.getCorreo())) {
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.CORREO_REGISTRADO);
				resJSON.setPayload(mapaMensajes);
			} else {
				usuarioRequest.setId(UUID.randomUUID().toString());
				//Se encripta la password
				//usuarioRequest.setContraseña(utils.encriptarPass(usuarioRequest.getContraseña()));
				Usuario usuario = new Usuario();
				usuario = usuarioConverter.resquestToEntity(usuarioRequest);
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
		logger.info("verUsuarios email :" + email);
		logger.info("verUsuarios TOKEN :" + token);		
		logger.info("verUsuarios token valido");
		resJSON.setDescripcion("OK");
		resJSON.setStatus(200);
		resJSON.setPayload(usuarioService.verUsuarios());
		
		
		
		/*if (utils.isTokenValid(token)) {
			logger.info("listarUsuarios token valido");
			resJSON.setDescripcion("OK");
			resJSON.setStatus(200);
			resJSON.setPayload(usuarioRepository.listarUsuarios());
		} else {
			logger.info("listarUsuarios token invalido");
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			Map<String, String> mapaMensajes = new HashMap<>();
			mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
			resJSON.setPayload(mapaMensajes);
		}*/
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
			UsuarioResponse usuarioResponse = new UsuarioResponse();
			/*
			 * loginRequest.setContraseña(utils.encriptarPass(loginRequest.getContraseña()))
			 * ; logger.info("Login Password Encriptarada: " +loginRequest.getContraseña());
			 */
			//usuarioResponse = usuarioRepository.login(loginRequest);
			usuarioResponse = usuarioService.login(loginRequest);
			if (usuarioResponse.getEmail() != null && usuarioResponse.getEmail() != "") {
				//usuarioRepository.actualizarFechaLogin(usuarioResponse.getEmail());
				usuarioService.actualizarFechaLogin(usuarioResponse.getEmail());
				if (utils.isTokenValid(token)) {
					resJSON.setDescripcion("OK");
					resJSON.setStatus(200);
					resJSON.setPayload(usuarioResponse);
				} else {
					logger.info("Login token invalido");
					resJSON.setDescripcion("ERROR");
					resJSON.setStatus(400);
					Map<String, String> mapaMensajes = new HashMap<>();
					mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
					resJSON.setPayload(mapaMensajes);
				}
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
	public ResponseJSON eliminarUsuario(@PathVariable("email") String email, @RequestHeader(value = "token") String token) {
		logger.info("eliminarUsuario init");
		logger.info("eliminarUsuario email: " + email);
		if (utils.validarEmail(email)) {
			if (utils.isTokenValid(token)) {
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
				logger.info("eliminarUsuario token invalido");
				resJSON.setDescripcion("ERROR");
				resJSON.setStatus(400);
				Map<String, String> mapaMensajes = new HashMap<>();
				mapaMensajes.put("mensaje", Constantes.TOKENINVALIDO);
				resJSON.setPayload(mapaMensajes);
			}
		} else {
			resJSON.setDescripcion("ERROR");
			resJSON.setStatus(400);
			Map<String, String> mapaMensajes = new HashMap<>();
			mapaMensajes.put("mensaje", Constantes.CORREO_SIN_FORMATO);
			resJSON.setPayload(mapaMensajes);
		}
		return resJSON;
	}

	///////////////////
	// Modificar un Usario
	@PostMapping("/modificarUsuario")
	public ResponseJSON modificarUSuario(@Valid @RequestBody UsuarioRequest usuarioRequest, BindingResult bindingResult, @RequestHeader(value = "token") String token) {
		logger.info("modificarUSuario init");
		if (utils.isTokenValid(token)) {
			
			//usuarioRepository.mo
			
			
			Map<String, String> mapaMensajes = new HashMap<>();
			resJSON.setDescripcion("OK");
			resJSON.setStatus(200);
			if (usuarioRepository.eliminarUsuario("")) {
				mapaMensajes.put("mensaje", "Usuario Eliminado");
			} else {
				mapaMensajes.put("mensaje", "Error al eliminar el usuario de correo: " + "");
			}
			resJSON.setPayload(mapaMensajes);
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

}