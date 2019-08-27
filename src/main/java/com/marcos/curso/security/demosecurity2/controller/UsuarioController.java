package com.marcos.curso.security.demosecurity2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marcos.curso.security.demosecurity2.domain.Usuario;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroProAdmParaAdminMedicoPaciente(Usuario usuario ) {
		return "usuario/cadastro";
	}
}