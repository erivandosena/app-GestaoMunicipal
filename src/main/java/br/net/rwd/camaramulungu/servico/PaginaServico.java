package br.net.rwd.camaramulungu.servico;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.net.rwd.camaramulungu.dao.DAOGenerico;
import br.net.rwd.camaramulungu.dao.PaginaDAO;
import br.net.rwd.camaramulungu.entidade.Pagina;

@Service("paginaServico")
public class PaginaServico extends DAOGenerico<Serializable> {

	@Autowired
	private PaginaDAO dao;
	
	protected void setDao(PaginaDAO dao) {
		this.dao = dao;
	}
	
	public Pagina incluirPagina(Pagina pagina) {
		return dao.adicionar(pagina);
	}
	
	public void alterarPagina(Pagina pagina) {
		dao.atualizar(pagina);
	}
	
	public void excluirPagina(Pagina pagina) {
		dao.remover(pagina);
	}

	public Pagina selecionarPagina(int codigo) {
		return dao.obterEntidade(Pagina.class, "SELECT p FROM Pagina p WHERE p.pag_cod = ?1", codigo);
	}
	
	public List<Pagina> listaPaginasMenu(String nome) {
		return dao.obterLista(Pagina.class, "SELECT p FROM Pagina p WHERE p.pag_posicao = ?1 ORDER BY p.pag_cod DESC", nome);
	}
	
	public List<Pagina> listarPagina(String nome) {
		return dao.obterLista(Pagina.class, "SELECT p FROM Pagina p WHERE lower(p.pag_titulo) like ?1", "%"+ nome.toLowerCase() + "%");
	}
	
	public List<Pagina> listarPagina() {
		return dao.obterLista(Pagina.class, "SELECT p FROM Pagina p ORDER BY p.pag_cod ASC");
	}
	
}
