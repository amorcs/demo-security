package com.marcos.curso.security.demosecurity2.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping({"/", "/home"})
	public String home() {
		return "home";
	}
	
	//abrir página login
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	
	@GetMapping({"/login-error"})
	public String loginError(ModelMap modelMap) {
		modelMap.addAttribute("alerta", "erro");
		modelMap.addAttribute("titulo", "credenciais inválidas");

		modelMap.addAttribute("texto", "Login ou senha incorretos");
		modelMap.addAttribute("subtexto", "acesso permitido apenas para cadastros já ativados");
		return "login";
	}
	
	@GetMapping({"/acesso-negado"})
	public String acessoNegado(ModelMap modelMap, HttpServletResponse response) {
		modelMap.addAttribute("status", response.getStatus());
		modelMap.addAttribute("error", "Acesso Negado");

		modelMap.addAttribute("message", "Você não tem permissão para este diretório");
		return "error";
	}
}
