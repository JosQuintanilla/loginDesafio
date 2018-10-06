package com.accenture.login.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.accenture.login.model.Usuario;

@Repository("usuarioRepository")
public class UsuarioRepositoryImp implements UsuarioRepository {

	static final String CreateUsuario = "INSERT INTO usuario (id, created, modified, last_login, token, name, email, password)	VALUES (?,?,?,?,?,?,?,?)";
	static final String ListarUsuarios = "select id, created, modified, last_login, token, name, email, password from usuario";
	private final JdbcTemplate jdbcTemplate;	
	
	@Autowired
	public UsuarioRepositoryImp(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public void save(Usuario usuario) {
		System.out.println("UsuarioRepositoryImp - save init");
		try {
			jdbcTemplate.update(CreateUsuario, usuario.getId(), usuario.getCreated(), usuario.getModified(), usuario.getLast_login(), usuario.getToken(), usuario.getName(), usuario.getEmail(), usuario.getPassword()); 
		} catch (Exception e) {
			System.out.println("UsuarioRepositoryImp - ERROR: ");
			e.printStackTrace();
		}
	}
	
	@Transactional
	public List<Usuario> listarUsuarios(){
		List<Usuario> listarUsuarios = new ArrayList<>();
		try{
            return jdbcTemplate.query(ListarUsuarios, new ResultSetExtractor<List<Usuario>>() {
            	 @Override  
                 public List<Usuario> extractData(ResultSet rs) throws SQLException, DataAccessException {
            		 List<Usuario> listarUsuarios = new ArrayList<>();
            		 while(rs.next()){ 
            			 Usuario usuario = new Usuario();
            			 //id
            			 usuario.setId(rs.getString(1));
            			 //created
            			 usuario.setCreated(rs.getDate(2));
            			 //modified
            			 usuario.setModified(rs.getDate(3));
            			 //last_login
            			 usuario.setLast_login(rs.getDate(4));
            			 //token
            			 usuario.setToken(rs.getString(5));
            			 //name
            			 usuario.setName(rs.getString(6));
            			 //email
            			 usuario.setEmail(rs.getString(7));
            			 //password
            			 usuario.setPassword(rs.getString(8));
            			 listarUsuarios.add(usuario);
            		 }
            		 return listarUsuarios;
            	 }
            });
        }catch (EmptyResultDataAccessException emptyData){
            return listarUsuarios;
        }
	}
	
	/**
	 * 
	 * public List<Employee> getAllEmployees(){  
 return template.query("select * from employee",new ResultSetExtractor<List<Employee>>(){  
    @Override  
     public List<Employee> extractData(ResultSet rs) throws SQLException,  
            DataAccessException {  
      
        List<Employee> list=new ArrayList<Employee>();  
        while(rs.next()){  
        Employee e=new Employee();  
        e.setId(rs.getInt(1));  
        e.setName(rs.getString(2));  
        e.setSalary(rs.getInt(3));  
        list.add(e);  
        }  
        return list;  
        }  
    });  
  }  
	 */

}