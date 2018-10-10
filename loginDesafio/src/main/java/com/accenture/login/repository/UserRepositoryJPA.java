package com.accenture.login.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.accenture.login.entity.Usuario;

@Repository("userRepositoryJPA")
@Transactional
public interface UserRepositoryJPA  extends JpaRepository<Usuario, Long>{

	@Query("select u from Usuario u")
	public List<Usuario> listarUsuarios();
	
	@Query("select u from Usuario u where u.email = :email")
	public Usuario findUsuarioByEmail(@Param("email") String email);
	
	@Query("select u from Usuario u where u.email = :email and u.password = :password")
	public Usuario loginUser(@Param("email") String email, @Param("password") String password);
	
	@Query("update Usuario set last_login = :fecha where email = :email")
	public abstract void actualziarFechaLogin(@Param("fecha") Date fecha, @Param("email") String email);
	
}