package com.marcos.curso.security.demosecurity2.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.curso.security.demosecurity2.datatables.Datatables;
import com.marcos.curso.security.demosecurity2.datatables.DatatablesColunas;
import com.marcos.curso.security.demosecurity2.domain.Especialidade;
import com.marcos.curso.security.demosecurity2.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

	@Autowired
	private EspecialidadeRepository especialidadeRepository;
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		especialidadeRepository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page = datatables.getSearch().isEmpty() 
				? especialidadeRepository.findAll(datatables.getPageable())
						:especialidadeRepository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Especialidade buscaProId(Long id) {
		return especialidadeRepository.findById(id).get();
	}
	@Transactional(readOnly = false)
	public void remover(Long id) {
		especialidadeRepository.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<String> buscarEspecialidadeByTermo(String termo) {
		return especialidadeRepository.findEspecialidadeByTermo(termo);
	}

	@Transactional(readOnly = true)
	public Set<Especialidade> buscarPorTitulos(String[] titulos) {
		return especialidadeRepository.findByTitulos(titulos);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
			datatables.setRequest(request);
			datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
			Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, datatables.getPageable());			
		return datatables.getResponse(page);
	}
	
}
