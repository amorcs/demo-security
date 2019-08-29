package com.marcos.curso.security.demosecurity2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.marcos.curso.security.demosecurity2.domain.PerfilTipo;
import com.marcos.curso.security.demosecurity2.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO= PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/", "/home").permitAll()
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			//acessos privados admin
			.antMatchers("/u/**").hasAuthority(ADMIN)
			//acessos privados medico
			.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/medicos/**").hasAnyAuthority(MEDICO)
			//acessos privados especialidades
			.antMatchers("/especialidades/**").hasAnyAuthority(ADMIN)
			//acessos privados pacientes
			.antMatchers("/pacientes/**").hasAnyAuthority(PACIENTE)
			//instrução para login
			.anyRequest().authenticated()
			.and()
				.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/", true)
					.failureUrl("/login-error")
					.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.exceptionHandling()
				.accessDeniedPage("/acesso-negado");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}
	

}
