package com.accenture.login.repository;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.accenture.login.model.LoginRequest;
import com.accenture.login.model.Usuario;
import com.accenture.login.model.UsuarioMapper;

@Repository("usuarioRepository")
public class UsuarioRepositoryImp implements UsuarioRepository {
	
	private static final String CreateUsuario = "INSERT INTO usuario (id, created, modified, last_login, token, name, email, password)	VALUES (?,?,?,?,?,?,?,?)";
	private static final String ListarUsuarios = "select id, created, modified, last_login, token, name, email, password from usuario";
	private static final String EliminarUsuario = "delete from usuario where email = ?";
	private static final String BuscarUsuarioXEmail = "select id, created, modified, last_login, token, name, email, password from usuario where email =?";
	private static final String Login = "select id, created, modified, last_login, token, name, email, password from usuario where email =? and password = ?";
	
	private final static Logger logger = Logger.getLogger(UsuarioRepositoryImp.class);
	private final JdbcTemplate jdbcTemplate;	
	
	@Autowired
	public UsuarioRepositoryImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public Usuario regitrarUsaurio(Usuario usuario) {
		logger.info("regitrarUsaurio - init");
		try {
			jdbcTemplate.update(CreateUsuario, usuario.getId(), usuario.getCreated(), usuario.getModified(), usuario.getLast_login(), usuario.getToken(), usuario.getName(), usuario.getEmail(), usuario.getPassword()); 
		} catch (Exception e) {
			logger.info("registrar Usuario Error: "+e.toString());
			e.printStackTrace();
		}
		return usuario;
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
	
	@Transactional
	public boolean eliminarUsuario(String email) {
		logger.info("eliminarUsuario - init");
		try {
			jdbcTemplate.update(EliminarUsuario, email);
			return true;
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
	public Usuario login(LoginRequest loginRequest) {
		logger.info("login - init");
		Usuario usuario = new Usuario();
		try {
			usuario = (Usuario) jdbcTemplate.queryForObject(Login, new Object[] { loginRequest.getCorreo(), loginRequest.getContraseña() }, new UsuarioMapper());	
		} catch (Exception e) {
			logger.info("login - ERROR: "+e.toString());
		}
		return usuario;
	}
	
	@Transactional
	public List<Usuario> listarUsuarios(){
		logger.info("listarUsuario . init");
		List<Usuario> listarUsuarios = new ArrayList<>();
		try{
			logger.info("listarUsuario try");
			listarUsuarios =jdbcTemplate.query(ListarUsuarios, new UsuarioMapper());
			return listarUsuarios;
        }catch (EmptyResultDataAccessException emptyData){
        	logger.info("listarUsuario . ERROR empyData");
            return listarUsuarios;
        }
	}
	
	/**
	 * 
	 public Item getItem(int itemId){
        String query = "SELECT * FROM ITEM WHERE ID=?";
        Item item = template.queryForObject(query,new Object[]{itemId},new BeanPropertyRowMapper<>(Item.class));

        return item;
    }
	
	public Article getArticleById(int articleId) {
		String sql = "SELECT articleId, title, category FROM articles WHERE articleId = ?";
		RowMapper<Article> rowMapper = new BeanPropertyRowMapper<Article>(Article.class);	
		Article article = jdbcTemplate.queryForObject(sql, rowMapper, articleId);
		return article;
	}
	
	@Transactional
	public List<Usuario> listarUsuarios(){
		logger.info("listarUsuario . init");
		List<Usuario> listarUsuarios = new ArrayList<>();
		try{
            return jdbcTemplate.query(ListarUsuarios, new ResultSetExtractor<List<Usuario>>() {
            	 @Override  
                 public List<Usuario> extractData(ResultSet rs) throws SQLException, DataAccessException {
            		 while(rs.next()){ 
            			 Usuario usuario = new Usuario();
            			 usuario.setId(rs.getString(1));
            			 usuario.setCreated(rs.getDate(2));
            			 usuario.setModified(rs.getDate(3));
            			 usuario.setLast_login(rs.getDate(4));
            			 usuario.setToken(rs.getString(5));
            			 usuario.setName(rs.getString(6));
            			 usuario.setEmail(rs.getString(7));
            			 usuario.setPassword(rs.getString(8));
            			 listarUsuarios.add(usuario);
            		 }
            		 return listarUsuarios;
            	 }
            });
        }catch (EmptyResultDataAccessException emptyData){
        	logger.info("listarUsuario . ERROR empyData");
            return listarUsuarios;
        }
	}
	 * @return
	 */
}