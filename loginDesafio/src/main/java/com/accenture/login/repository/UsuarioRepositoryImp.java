package com.accenture.login.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.accenture.login.converter.UsuarioConverter;
import com.accenture.login.converter.UsuarioMapper;
import com.accenture.login.converter.UsuarioResponseMapper;
import com.accenture.login.entity.Telefono;
import com.accenture.login.entity.Usuario;
import com.accenture.login.model.LoginRequest;
import com.accenture.login.model.UsuarioResponse;

@Repository("usuarioRepository")
public class UsuarioRepositoryImp implements UsuarioRepository {
	
	private static final String CreateUsuario = "INSERT INTO usuario (id, created, modified, last_login, token, name, email, password)	VALUES (?,?,?,?,?,?,?,?)";
	private static final String InsertTelefonos = "INSERT INTO telefono ( number, citycode, contrycode, id)	VALUES (?,?,?,?)";
	private static final String ListarUsuarios = "select u.name, u.email, u.password, u.token, t.number, t.citycode, t.contrycode from usuario as u, telefono as t where u.id = t.id  group by u.id, u.name, u.email, u.token, t.number, t.citycode, t.contrycode";
	private static final String listarTelefonos = "select email, number, citycode, contrycode from telefon where email = ?)";
	private static final String EliminarUsuario = "delete from usuario u JOIN telefono t on u.email = t.email where u.email = ?";
	private static final String BuscarUsuarioXEmail = "select id, created, modified, last_login, token, name, email, password from usuario where email =?";
	private static final String Login = "select u.name, u.email, u.password, u.token, t.number, t.citycode, t.contrycode from usuario as u, telefono as t where u.id = t.id AND u.email =? and u.password = ?";
	private static final String ActualizarFechaLogin = "update usuario set last_login = ? where email = ?";
	
	
	private final static Logger logger = Logger.getLogger(UsuarioRepositoryImp.class);
	private final JdbcTemplate jdbcTemplate;	
	
	@Autowired
	@Qualifier("usuarioConverter")
	private UsuarioConverter usuarioConverter;
	
	@Autowired
	public UsuarioRepositoryImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public Usuario regitrarUsaurio(Usuario usuario) {
		logger.info("regitrarUsaurio - init");
		try {
			jdbcTemplate.update(CreateUsuario, usuario.getId(), usuario.getCreated(), usuario.getModified(), usuario.getLast_login(), usuario.getToken(), usuario.getName(), usuario.getEmail(), usuario.getPassword());
			this.registrarTelefonos(usuario.getPhones(), usuario.getId());
		} catch (Exception e) {
			logger.info("registrar Usuario Error: "+e.toString());
			e.printStackTrace();
		}
		return usuario;
	}
	
	@Transactional
	public void registrarTelefonos(List<Telefono> list, String id) {
		logger.info("registrarTelefonos - init");
		try {
			for (Telefono telefono : list) {
				jdbcTemplate.update(InsertTelefonos, telefono.getNumber().toString(), telefono.getCitycode().toString(), telefono.getContrycode().toString(), id);
			}			 
		} catch (Exception e) {
			logger.info("registrarTelefonos Error: "+e.toString());
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public void save(Usuario usuario) {
		logger.info("save - init");
		try {
			jdbcTemplate.update(CreateUsuario, usuario.getId(), usuario.getCreated(), usuario.getModified(), usuario.getLast_login(), usuario.getToken(), usuario.getName(), usuario.getEmail(), usuario.getPassword()); 
		} catch (Exception e) {
			logger.info("save - ERROR: "+e.toString());
			e.printStackTrace();
		}
	}
	
	public void actualizarFechaLogin(String email) {
		logger.info("actualizarFechaLogin - init");
		try {
			jdbcTemplate.update(ActualizarFechaLogin, new Date(), email);
		} catch (Exception e) {
			logger.info("actualizarFechaLogin - ERROR: "+e.toString());
			e.printStackTrace();
		}
	}
	
	
	@Transactional
	public boolean eliminarUsuario(String email) {
		logger.info("eliminarUsuario - init");
		try {
			if(jdbcTemplate.update(EliminarUsuario, email) == 0) {
				logger.info("eliminarUsuario - IF");
				return false;
			}else {
				logger.info("eliminarUsuario - ELSE");
				return true;
			}
			
		}catch (Exception e) {
			logger.info("eliminarUsuario - ERROR: "+e.toString());
			return false;
		}
	}
	
	@Transactional
	public boolean existeUsuario(String email) {
		logger.info("existeUsuario - init");
		logger.info("existeUsuario - Email: "+email);
		Boolean existeEmail = false;
		try {
			Usuario usuario = new Usuario();			
			usuario = (Usuario) jdbcTemplate.queryForObject(BuscarUsuarioXEmail, new Object[] { email }, new UsuarioMapper());			
			logger.info("existeUsuario - registrOOOOOOOOo: "+usuario.getEmail());
			if(usuario.getEmail() != null || usuario.getEmail() != "") {
				existeEmail =  true;
			}else {
				existeEmail = false;
			}
		} catch (Exception e) {
			logger.info("existeUsuario - ERROR: "+e.toString());
		}
		return existeEmail;
	}
	
	@Transactional
	public UsuarioResponse login(LoginRequest loginRequest) {
		logger.info("login - init");
		UsuarioResponse usuarioResponse = new UsuarioResponse();
		List<UsuarioResponse> listaUsuarioResponse = new ArrayList<>();
		try {
			logger.info("login - try");
			listaUsuarioResponse = jdbcTemplate.query(Login, new Object[] { loginRequest.getCorreo(), loginRequest.getContraseña() }, new UsuarioResponseMapper());
			listaUsuarioResponse = usuarioConverter.ordenarUsuarioResponse(listaUsuarioResponse);
			usuarioResponse = listaUsuarioResponse.get(0);
		} catch (Exception e) {
			logger.info("login - ERROR: "+e.toString());
		}
		return usuarioResponse;
	}
	
	@Transactional
	public List<UsuarioResponse> listarUsuarios(){
		logger.info("listarUsuario . init");
		List<UsuarioResponse> listarUsuarios = new ArrayList<>();
		try{
			logger.info("listarUsuario try");
			listarUsuarios =jdbcTemplate.query(ListarUsuarios, new UsuarioResponseMapper());
			return usuarioConverter.ordenarUsuarioResponse(listarUsuarios);
        }catch (EmptyResultDataAccessException emptyData){
        	logger.info("listarUsuario . ERROR empyData");
            return listarUsuarios;
        }
	}
}