package com.marcos.curso.security.demosecurity2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marcos.curso.security.demosecurity2.domain.Medico;
import com.marcos.curso.security.demosecurity2.domain.Usuario;
import com.marcos.curso.security.demosecurity2.service.MedicoService;
import com.marcos.curso.security.demosecurity2.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService medicoService;
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap modelMap, @AuthenticationPrincipal User user) {
		if (medico.hasNotId()) {
			medico = medicoService.buscarPorEmail(user.getUsername());
			modelMap.addAttribute("medico", medico);
		}
		return "medico/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvarMedico(Medico medico, RedirectAttributes attr,
					@AuthenticationPrincipal User user) {
		if (medico.hasNotId() && medico.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.buscaPorEmail(user.getUsername());
			medico.setUsuario(usuario);
		}		
		medicoService.salvar(medico);
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso");
		attr.addFlashAttribute("medico",medico);
		return "redirect:/medicos/dados";
	}
	
	@PostMapping("editar")
	public String editar(Medico medico, RedirectAttributes attr) {
		medicoService.editar(medico);
		attr.addFlashAttribute("sucesso","Operação realizada com sucesso");
		attr.addFlashAttribute("medico",medico);
		return "medico/cadastro";
	}
	
			
}
