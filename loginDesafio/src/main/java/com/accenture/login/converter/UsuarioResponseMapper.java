package com.accenture.login.converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.accenture.login.model.TelefonoModel;
import com.accenture.login.model.UsuarioResponse;

public class UsuarioResponseMapper implements RowMapper {

	@Override
	public UsuarioResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		UsuarioResponse response = new UsuarioResponse();
		List<TelefonoModel> listaTelefonos = new ArrayList<>();
		TelefonoModel tefonoModel = new TelefonoModel();
		response.setName(rs.getString(1));
		response.setEmail(rs.getString(2));
		response.setPassword(rs.getString(3));
		response.setToken(rs.getString(4));		
		tefonoModel.setNumber(rs.getInt(5));
		tefonoModel.setCitycode(rs.getInt(6));
		tefonoModel.setContrycode(rs.getInt(7));
		listaTelefonos.add(tefonoModel);
		response.setPhones(listaTelefonos);
		return response;
	}
}
