package com.marcos.curso.security.demosecurity2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.curso.security.demosecurity2.domain.Medico;
import com.marcos.curso.security.demosecurity2.repository.MedicoRepository;

@Service
public class MedicoService {
	@Autowired
	private MedicoRepository medicoRepository;
	
	
	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(Long id) {
		return medicoRepository.findByUsuarioId(id).orElse(new Medico());
	}
	
	@Transactional(readOnly = false)
	public void salvar(Medico medico) {
		medicoRepository.save(medico);
	}

	@Transactional(readOnly = false)
	public void editar(Medico medico) {
		Medico m2 = medicoRepository.findById(medico.getId()).get();
		m2.setCrm(medico.getCrm());
		m2.setDtInscricao(medico.getDtInscricao());
		m2.setNome(medico.getNome());
		if (!medico.getEspecialidades().isEmpty()) {
			m2.getEspecialidades().addAll(medico.getEspecialidades());
		}
			medicoRepository.save(m2);
		
	}

	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(String username) {
		return medicoRepository.findByUsuarioEmail(username).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void excluirEspecialidadePorMedicoId(Long idMed, Long idEsp) {
		Medico medico = medicoRepository.findById(idMed).get();
		medico.getEspecialidades().removeIf(esp -> esp.getId().equals(idEsp));
	}
}
