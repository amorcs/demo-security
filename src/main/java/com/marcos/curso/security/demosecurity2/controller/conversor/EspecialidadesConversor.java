package com.marcos.curso.security.demosecurity2.controller.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.marcos.curso.security.demosecurity2.domain.Especialidade;
import com.marcos.curso.security.demosecurity2.service.EspecialidadeService;

@Component
public class EspecialidadesConversor implements Converter<String[], Set<Especialidade>>{
	@Autowired
	private EspecialidadeService especialidadeService;
	@Override
	public Set<Especialidade> convert(String[] titulos) {
		Set<Especialidade> especialidades = new HashSet<Especialidade>();
		if (titulos != null && titulos.length > 0) {
			especialidades.addAll(especialidadeService.buscarPorTitulos(titulos));
		}
		return especialidades;
	}
	
}
