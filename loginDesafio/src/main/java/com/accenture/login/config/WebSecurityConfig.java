package com.accenture.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		  http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/api/registrarUsuario");
		  
		  http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/registrarUsuario").permitAll().and()
		      .authorizeRequests().antMatchers("/console/**").permitAll();

		  http.headers().frameOptions().disable(); 

	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
