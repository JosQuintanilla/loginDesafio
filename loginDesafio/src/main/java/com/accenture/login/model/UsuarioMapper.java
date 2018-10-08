package com.accenture.login.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.accenture.login.entity.Usuario;

public class UsuarioMapper implements RowMapper {

	@Override
	public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
		Usuario usuario = new Usuario();
		usuario.setId(rs.getString(1));
		usuario.setCreated(rs.getDate(2));
		usuario.setModified(rs.getDate(3));
		usuario.setLast_login(rs.getDate(4));
		usuario.setToken(rs.getString(5));
		usuario.setName(rs.getString(6));
		usuario.setEmail(rs.getString(7));
		usuario.setPassword(rs.getString(8));
		return usuario;
	}

}