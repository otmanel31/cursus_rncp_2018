package com.loncoto.firstsecurityform.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		if (myPasswordEncoder == null)
			myPasswordEncoder = new BCryptPasswordEncoder(); 
		return myPasswordEncoder;
	}
	
	private PasswordEncoder myPasswordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// le parametre auth passé ici permet de configurer tout ce qui concerne
		// l'authentification/gestion des comptes utilisateurs/identités
		// cet objet utilise le pattern builder
		
	
		// exemple basique avec utilisteurs en "dur" stocké en mémoire
		/*auth.inMemoryAuthentication()
			.withUser("admin").password("admin").roles("ADMIN", "USER").and()
			.withUser("vincent").password("1234").roles("USER").and()
			.withUser("elon").password("marslove").roles("VISITOR");
		*/
		// gestion des utilisateurs via base de donnée
		// via jdbc
		/*
		auth.jdbcAuthentication().usersByUsernameQuery("sql...") // username, password, enabled
								 .authoritiesByUsernameQuery("sql...."); //username, authority
		*/
		
		//on veut récupérer nos utilisateurs/roles via jpa (entites)
		// fournir notre propre service de récupération des utilisateurs/roles
		
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// l'objet HttpSecuroty passé en parametre permet de configurer
		// les droits d'access, et plein d'autres choses (gestion du login,
		// session, cors, csrf, etc...)
		
		// meme design pattern qu'au dessus, le pattern builder
		
		http.authorizeRequests().antMatchers("/admin").hasRole("ADMIN")
								.antMatchers("/client").hasAnyRole("ADMIN", "USER")
								.antMatchers("/public").authenticated()
								.antMatchers("/register").permitAll()
								.antMatchers("/").permitAll()
			.and().httpBasic()
			.and().csrf().disable();
	}

	
	
}
