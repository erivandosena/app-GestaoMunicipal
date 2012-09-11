package br.net.rwd.camaramulungu.servico;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.net.rwd.camaramulungu.dao.DAOGenerico;
import br.net.rwd.camaramulungu.dao.UsuarioDAO;
import br.net.rwd.camaramulungu.entidade.Usuario;

@Service("usuarioServico")
public class UsuarioServico extends DAOGenerico<Serializable> {// extends HibernateDaoSupport implements UserDetailsService {

	@Autowired
	private UsuarioDAO dao;
	
	protected void setDao(UsuarioDAO dao) {
		this.dao = dao;
	}
	
	public Usuario incluirUsuario(Usuario usuario) {
		return dao.adicionar(usuario);
	}
	
	public void alterarUsuario(Usuario usuario) {
		dao.atualizar(usuario);
	}
	
	public void excluirUsuario(Usuario usuario) {
		dao.remover(usuario);
	}
	
	public Usuario selecionarUsuarios() {
		return dao.obterEntidade(Usuario.class, "SELECT u FROM Usuario u");
	}
	
	public Usuario selecionarUsuario(int codigo) {
		return dao.obterEntidade(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_cod = ?1", codigo);
	}
	
	public Usuario selecionarUsuario(String nome) {
		return dao.obterEntidade(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_nome = ?1", nome);
	}
	
	public Usuario selecionarUsuarioLogin(String login) {
		return dao.obterEntidade(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_email = ?1", login);
	}
	
	public List<Usuario> listarUsuarios() {
		return dao.obterLista(Usuario.class, "SELECT u FROM Usuario u ORDER BY u.usu_cod ASC");
	}
	
	public List<Usuario> listarUsuario(int codigo) {
		return dao.obterLista(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_cod = ?1", codigo);
	}
	
	public List<Usuario> listarUsuario(String nome) {
		return dao.obterLista(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_nome = ?1", nome);
	}
	
	public List<Usuario> listarLikeUsuario(String nome) {
		return dao.obterLista(Usuario.class, "SELECT u FROM Usuario u WHERE u.usu_nome like ?1", nome);
	}
	
}
