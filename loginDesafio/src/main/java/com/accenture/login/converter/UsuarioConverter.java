package com.accenture.login.converter;

import org.springframework.stereotype.Component;

import com.accenture.login.model.Usuario;
import com.accenture.login.model.UsuarioRequest;

@Component("usuarioConverter")
public class UsuarioConverter {

	public Usuario resquestToModel(UsuarioRequest usuarioRequest) {
		Usuario usuario = new Usuario();
		if(usuarioRequest != null) {
			usuario.setName(usuarioRequest.getNombre());
			usuario.setEmail(usuarioRequest.getCorreo());
			usuario.setPassword(usuarioRequest.getContraseña());
		}else {
			usuario.setName("");
			usuario.setEmail("");
			usuario.setPassword("");
		}				
		return usuario;
	}
	
	/** 
	public Usuario modelToEntity(UsuarioModel usuarioModel) {
		Usuario usuario = new Usuario();
		if(usuarioModel != null) {
			usuario.setId(usuarioModel.getId());
			usuario.setName(usuarioModel.getName());
			usuario.setEmail(usuarioModel.getEmail());
			usuario.setPassword(usuarioModel.getPassword());
			usuario.setCreated(usuarioModel.getCreated());
			usuario.setModified(usuarioModel.getModified());
			usuario.setLast_login(usuarioModel.getLast_login());
			usuario.setToken(usuarioModel.getToken());			
			List<Telefono> listaPhones = new ArrayList<>();		
			for (TelefonoModel telefonoModel: usuarioModel.getPhones()) {
				Telefono telefono = new Telefono();
				telefono.setCitycode(telefonoModel.getCitycode());
				telefono.setContrycode(telefonoModel.getContrycode());
				telefono.setNumber(telefonoModel.getNumber());
				listaPhones.add(telefono);
			}		
			usuario.setPhones(listaPhones);
		}else {
			usuario.setId("");
			usuario.setName("");
			usuario.setEmail("");
			usuario.setPassword("");
			usuario.setCreated(new Date());
			usuario.setModified(new Date());
			usuario.setLast_login(new Date());
			usuario.setToken("");			
			List<Telefono> listaPhones = new ArrayList<>();				
			usuario.setPhones(listaPhones);
		}		
		return usuario;
	}
	
	public UsuarioModel entityToModel(Usuario usuario) {
		UsuarioModel usuarioModel = new UsuarioModel();
		if(usuario != null) {
			usuarioModel.setId(usuario.getId());
			usuarioModel.setName(usuario.getName());
			usuarioModel.setEmail(usuario.getEmail());
			usuarioModel.setPassword(usuario.getPassword());
			usuarioModel.setCreated(usuario.getCreated());
			usuarioModel.setLast_login(usuario.getLast_login());
			usuarioModel.setModified(usuario.getModified());
			usuarioModel.setToken(usuario.getToken());				
			List<TelefonoModel> listaTelefonoModel = new ArrayList<>();
			for (Telefono telefono: usuario.getPhones()) {
				TelefonoModel telefonoModel = new TelefonoModel();
				telefonoModel.setCitycode(telefono.getCitycode());
				telefonoModel.setContrycode(telefono.getContrycode());
				telefonoModel.setNumber(telefono.getNumber());
				listaTelefonoModel.add(telefonoModel);
			}	
			usuarioModel.setPhones(listaTelefonoModel);
		}else {
			usuarioModel.setId("");
			usuarioModel.setName("");
			usuarioModel.setEmail("");
			usuarioModel.setPassword("");
			usuarioModel.setCreated(new Date());
			usuarioModel.setLast_login(new Date());
			usuarioModel.setModified(new Date());
			usuarioModel.setToken("");				
			List<TelefonoModel> listaTelefonoModel = new ArrayList<>();
			usuarioModel.setPhones(listaTelefonoModel);
		}	
		return usuarioModel;
	}
	*/
}