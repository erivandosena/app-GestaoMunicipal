package br.net.rwd.camaramulungu.servico;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.net.rwd.camaramulungu.dao.ArquivoDAO;
import br.net.rwd.camaramulungu.dao.DAOGenerico;
import br.net.rwd.camaramulungu.entidade.Arquivo;

@Service("arquivoServico")
public class ArquivoServico extends DAOGenerico<Serializable> {
	
	@Autowired
	private ArquivoDAO dao;

	protected void setDao(ArquivoDAO dao) {
		this.dao = dao;
	}
	
	public Arquivo incluirArquivo(Arquivo arquivo) {
		return dao.adicionar(arquivo);
	}
	
	public void alterarArquivo(Arquivo arquivo) {
		dao.atualizar(arquivo);
	}
	
	public void excluirArquivo(Arquivo arquivo) {
		dao.remover(arquivo);
	}
	
	public Arquivo selecionarArquivos() {
		return dao.obterEntidade(Arquivo.class, "SELECT a FROM Arquivo a");
	}
	
	public Arquivo selecionarArquivo(int codigo) {
		return dao.obterEntidade(Arquivo.class, "SELECT a FROM Arquivo a WHERE a.arq_cod = ?1", codigo);
	}
	
	public Arquivo selecionarArquivo(String nome) {
		return dao.obterEntidade(Arquivo.class, "SELECT a FROM Arquivo a WHERE a.arq_nome = ?1", nome);
	}
	
	public List<Arquivo> listarArquivos() {
		return dao.obterLista(Arquivo.class, "SELECT a FROM Arquivo a ORDER BY a.arq_cod ASC");
	}
	
	public List<Arquivo> listarArquivo(int codigo) {
		return dao.obterLista(Arquivo.class, "SELECT a FROM Arquivo a WHERE a.arq_cod = ?1", codigo);
	}
	
	public List<Arquivo> listarArquivo(String nome) {
		return dao.obterLista(Arquivo.class, "SELECT a FROM Arquivo a WHERE a.arq_nome = ?1", nome);
	}
	
	public List<Arquivo> listarLikeArquivo(String nome) {
		return dao.obterLista(Arquivo.class, "SELECT a FROM Arquivo a WHERE a.arq_nome like ?1", nome);
	}
}
