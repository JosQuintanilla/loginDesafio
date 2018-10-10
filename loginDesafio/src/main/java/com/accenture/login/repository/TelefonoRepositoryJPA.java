package com.accenture.login.repository;

import java.io.Serializable;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.login.entity.Telefono;

@Repository("telefonoRepositoryJPA")
@Transactional
public interface TelefonoRepositoryJPA extends JpaRepository<Telefono, Serializable>{

}