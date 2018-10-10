package com.accenture.login.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.accenture.login.entity.Telefono;
import com.accenture.login.entity.Usuario;
import com.accenture.login.model.TelefonoModel;
import com.accenture.login.model.UsuarioRequest;
import com.accenture.login.model.UsuarioResponse;

@Component("usuarioConverter")
public class UsuarioConverter {
		
	public List<UsuarioResponse> listaUsuariosToListUsuarioResponse(List<Usuario> listarUsuarios){
		List<UsuarioResponse> listaUsuarios = new ArrayList<>();
		if(listarUsuarios != null) {
			for (Usuario usuario : listarUsuarios) {
				UsuarioResponse usuarioResponse = new UsuarioResponse();
				usuarioResponse.setName(usuario.getName());
				usuarioResponse.setEmail(usuario.getEmail());
				usuarioResponse.setPassword(usuario.getPassword());
				usuarioResponse.setToken(usuario.getToken());				
				if (usuario.getPhones() != null) {
					List<TelefonoModel> listaTelefonos = new ArrayList<>();
					for (Telefono telefono : usuario.getPhones()) {
						TelefonoModel telefonoModel = new TelefonoModel();
						telefonoModel.setNumber(telefono.getNumber());
						telefonoModel.setCitycode(telefono.getCitycode());
						telefonoModel.setContrycode(telefono.getContrycode());
						listaTelefonos.add(telefonoModel);
					}
					usuarioResponse.setPhones(listaTelefonos);
				} else {
					List<TelefonoModel> listaTelefonos = new ArrayList<>();
					usuarioResponse.setPhones(listaTelefonos);
				}			
				listaUsuarios.add(usuarioResponse);
			}
		}
		return listaUsuarios;
	}

	public List<Usuario> ordenarUsuarioTelefono(List<Usuario> listaUsuarios, List<Telefono> listaTelefonos){
		System.out.println("UsuarioConverter - ordenarUsuarioTelefono - init");
		if(listaTelefonos != null) {
			Map<String, List<Telefono>> mapaUsuarioTelefonos = new HashMap<>();
			for (Telefono telefono : listaTelefonos) {
				System.out.println("UsuarioConverter - ordenarUsuarioTelefono - for Telefono");
				System.out.println("UsuarioConverter - ordenarUsuarioTelefono - for Telefono Usuario ID: "+telefono.getUsuario().getId());
				List<Telefono> listaAux = new ArrayList<>();
				if(mapaUsuarioTelefonos.containsKey(telefono.getUsuario().getId())) {
					System.out.println("UsuarioConverter - ordenarUsuarioTelefono - for Telefono IF constrain id: "+telefono.getUsuario().getId());
					listaAux = mapaUsuarioTelefonos.get(telefono.getUsuario().getId());
					listaAux.add(telefono);
					mapaUsuarioTelefonos.put(telefono.getUsuario().getId(), listaAux);
				}else {
					System.out.println("UsuarioConverter - ordenarUsuarioTelefono - for Telefono ELSE constrain id: "+telefono.getUsuario().getId());
					listaAux.add(telefono);
					mapaUsuarioTelefonos.put(telefono.getUsuario().getId(), listaAux);
				}
			}
			//
			List<Usuario> listaUsuariosAux = new ArrayList<>(listaUsuarios);
			listaUsuarios = new ArrayList<Usuario>();
			for (Usuario usuario : listaUsuariosAux) {
				if(mapaUsuarioTelefonos.containsKey(usuario.getId())) {
					usuario.setPhones(mapaUsuarioTelefonos.get(usuario.getId()));					
					listaUsuarios.add(usuario);
				}else {
					listaUsuarios.add(usuario);
				}
			} 
		}		
		return listaUsuarios;
	}
	
	public Usuario resquestToEntity(UsuarioRequest usuarioRequest) {
		Usuario usuario = new Usuario();
		if (usuarioRequest != null) {
			usuario.setId(usuarioRequest.getId());
			usuario.setName(usuarioRequest.getNombre());
			usuario.setEmail(usuarioRequest.getCorreo());
			usuario.setPassword(usuarioRequest.getContraseña());
			if (usuarioRequest.getListaTelefonos() != null) {
				List<Telefono> listaPhones = new ArrayList<>();
				for (TelefonoModel telefonoModel : usuarioRequest.getListaTelefonos()) {
					Telefono telefono = new Telefono();
					telefono.setCitycode(telefonoModel.getCitycode());
					telefono.setContrycode(telefonoModel.getContrycode());
					telefono.setNumber(telefonoModel.getNumber());
					telefono.setUsuario(usuario);
					listaPhones.add(telefono);
				}
				usuario.setPhones(listaPhones);
			} else {
				List<Telefono> listaPhones = new ArrayList<>();
				usuario.setPhones(listaPhones);
			}
		} else {
			usuario.setName("");
			usuario.setEmail("");
			usuario.setPassword("");
			List<Telefono> listaPhones = new ArrayList<>();
			usuario.setPhones(listaPhones);
		}
		return usuario;
	}

	public UsuarioResponse entityToResponse(Usuario usuario) {
		UsuarioResponse usuarioResponse = new UsuarioResponse();
		if (usuario != null) {
			usuarioResponse.setName(usuario.getName());
			usuarioResponse.setEmail(usuario.getEmail());
			usuarioResponse.setPassword(usuario.getPassword());
			usuarioResponse.setToken(usuario.getToken());
			if (usuario.getPhones() != null) {
				List<TelefonoModel> listaTelefonos = new ArrayList<>();
				for (Telefono telefono : usuario.getPhones()) {
					TelefonoModel telefonoModel = new TelefonoModel();
					telefonoModel.setNumber(telefono.getNumber());
					telefonoModel.setCitycode(telefono.getCitycode());
					telefonoModel.setContrycode(telefono.getContrycode());
					listaTelefonos.add(telefonoModel);
				}
				usuarioResponse.setPhones(listaTelefonos);
			} else {
				List<TelefonoModel> listaTelefonos = new ArrayList<>();
				usuarioResponse.setPhones(listaTelefonos);
			}
		} else {
			usuarioResponse.setName("");
			usuarioResponse.setEmail("");
			usuarioResponse.setPassword("");
		}
		return usuarioResponse;
	}

	public List<UsuarioResponse> ordenarUsuarioResponse(List<UsuarioResponse> listaResponse) {
		List<UsuarioResponse> listaOrdenada = new ArrayList<>();
		Map<String, UsuarioResponse> mapaEmail = new HashMap<>();

		for (UsuarioResponse usuarioResponse : listaResponse) {
			UsuarioResponse usuarioAux = new UsuarioResponse();
			if (mapaEmail.containsKey(usuarioResponse.getEmail())) {

				usuarioAux = mapaEmail.get(usuarioResponse.getEmail());

				List<TelefonoModel> listaTelefonoModel = usuarioAux.getPhones();

				for (TelefonoModel telefonoModel : usuarioResponse.getPhones()) {
					listaTelefonoModel.add(telefonoModel);
				}

				usuarioAux.setPhones(listaTelefonoModel);

				mapaEmail.put(usuarioResponse.getEmail(), usuarioAux);

			} else {
				usuarioAux.setName(usuarioResponse.getName());
				usuarioAux.setEmail(usuarioResponse.getEmail());
				usuarioAux.setPassword(usuarioResponse.getPassword());
				usuarioAux.setToken(usuarioResponse.getToken());
				usuarioAux.setPhones(usuarioResponse.getPhones());
				mapaEmail.put(usuarioResponse.getEmail(), usuarioAux);
			}

		}
		// Recorro el Mapa
		for (UsuarioResponse usuarioResponseMap : mapaEmail.values()) {
			listaOrdenada.add(usuarioResponseMap);
		}

		return listaOrdenada;
	}
}