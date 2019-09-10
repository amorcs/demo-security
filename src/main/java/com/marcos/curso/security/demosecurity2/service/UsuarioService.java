package com.marcos.curso.security.demosecurity2.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcos.curso.security.demosecurity2.datatables.Datatables;
import com.marcos.curso.security.demosecurity2.datatables.DatatablesColunas;
import com.marcos.curso.security.demosecurity2.domain.Perfil;
import com.marcos.curso.security.demosecurity2.domain.Usuario;
import com.marcos.curso.security.demosecurity2.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = true)
	public Usuario buscaPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
				
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscaPorEmail(username);
		return new User(
				usuario.getEmail(),
				usuario.getSenha(),
				AuthorityUtils.createAuthorityList(getAuthority(usuario.getPerfis())));
	}
	
	private String [] getAuthority(List<Perfil> perfis) {
		String [] authorities = new String[perfis.size()];
		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}
		return authorities;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty()
				? usuarioRepository.findAll(datatables.getPageable()) 
						:
				usuarioRepository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(crypt);
		usuarioRepository.save(usuario);
	}
	@Transactional(readOnly = true)
	public Usuario buscaPorId(Long id) {
		return usuarioRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		return usuarioRepository.findByIdAndPerfis(usuarioId, perfisId).orElseThrow(
				()-> new UsernameNotFoundException("Usuário Inexistente"));
	}

	public static boolean isSenhaCorreta(String senhaEscrita, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaEscrita, senhaArmazenada);
	}
	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String novaSenha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(novaSenha));
		usuarioRepository.save(usuario);
	}
}
