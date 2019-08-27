package com.marcos.curso.security.demosecurity2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marcos.curso.security.demosecurity2.domain.Medico;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap modelMap) {
		return "medico/cadastro";
	}
}
