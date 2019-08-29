package com.marcos.curso.security.demosecurity2.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.marcos.curso.security.demosecurity2.domain.Medico;
import com.marcos.curso.security.demosecurity2.domain.Perfil;
import com.marcos.curso.security.demosecurity2.domain.PerfilTipo;
import com.marcos.curso.security.demosecurity2.domain.Usuario;
import com.marcos.curso.security.demosecurity2.service.MedicoService;
import com.marcos.curso.security.demosecurity2.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private MedicoService medicoService;
	
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroProAdmParaAdminMedicoPaciente(Usuario usuario ) {
		return "usuario/cadastro";
	}
	
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDataTables(HttpServletRequest request) {
		return ResponseEntity.ok(usuarioService.buscarTodos(request));
	}
	
	@PostMapping("/cadastro/salvar")
	public String salvarUsuario(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		
		if (perfis.size()>2||
				perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L)))
				||
				perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
			attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico");
			attr.addFlashAttribute("usuario", usuario);
		}else {
			try {
				usuarioService.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
			} catch (DataIntegrityViolationException ex) {
				attr.addFlashAttribute("falha", "Email já existente na Base de Dados");
			}
			
		}
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id")Long id) {
		return new ModelAndView("usuario/cadastro", "usuario", usuarioService.buscaPorId(id));
		
	}
	
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id")Long usuarioId, 
														@PathVariable("perfis") Long[]perfisId) {
		
		Usuario usuario = usuarioService.buscarPorIdEPerfis(usuarioId, perfisId);
		if (usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
				!usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			return new ModelAndView("usuario/cadastro", "usuario", usuario);
		}else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
				return medico.hasNotId() 
						? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
						: new ModelAndView("medico/cadastro", "medico", medico);
			
		}else if(usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área restrita");
			model.addObject("message", "Dados de paciente são restritos");
			return model;
		}
		
		return new ModelAndView("redirect:/u/lsita");
	}
	
	
	
	
}