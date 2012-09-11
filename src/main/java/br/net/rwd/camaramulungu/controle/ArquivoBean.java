package br.net.rwd.camaramulungu.controle;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import br.net.rwd.camaramulungu.entidade.Arquivo;
import br.net.rwd.camaramulungu.servico.ArquivoServico;

@ManagedBean(name = "arquivoBean")
@ViewScoped
public class ArquivoBean extends UtilBean implements CrudBeans<Object> {

	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{arquivoServico}")
	private ArquivoServico model;
	private Arquivo arquivo;
	private List<Arquivo> arquivos;
	private boolean modoEdicao;
	
	private Integer arq_cod;
	private String arq_nome;
	private String arq_local;
	
	public ArquivoServico getModel() {
		return model;
	}

	public void setModel(ArquivoServico model) {
		this.model = model;
	}

	public Arquivo getArquivo() {
		if (arquivo == null) {
			arquivo = new Arquivo();
		}
		return arquivo;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public List<Arquivo> getArquivos() {
		if (arquivos == null) {
			arquivos = model.listarArquivos();
		}
		return arquivos;
	}

	public void setArquivos(List<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public Integer getArq_cod() {
		return arq_cod;
	}

	public void setArq_cod(Integer arq_cod) {
		this.arq_cod = arq_cod;
	}

	public String getArq_nome() {
		return arq_nome;
	}

	public void setArq_nome(String arq_nome) {
		this.arq_nome = arq_nome;
	}

	public String getArq_local() {
		return arq_local;
	}

	public void setArq_local(String arq_local) {
		this.arq_local = arq_local;
	}
	
	/* ------------------------------------------------- */

	@Override
	public void incluir() {
        this.arquivo = new Arquivo();
        this.modoEdicao = true;
	}

	@Override
	public void salvar() {
        if (arquivo.getArq_cod() == null || arquivo.getArq_cod().intValue() == 0) {
            arquivo = model.incluirArquivo(arquivo);
            arquivo = new Arquivo();
            addInfoMessage("Arquivo upado com sucesso.");
            retornar();
        } else {
            model.alterarArquivo(arquivo);
            addInfoMessage("Arquivo alterado com sucesso.");
            retornar();
        }	
	}

	@Override
	public void atualizar() {
		this.modoEdicao = true;
	}

	@Override
	public void excluir() {
		model.excluirArquivo(arquivo);
		retornar();
	}

	@Override
	public void filtrar(AjaxBehaviorEvent event) {
        if (arq_nome != null && !arq_nome.isEmpty()) {
            arquivos = model.listarLikeArquivo("%"+arq_nome+"%");
        } else {
            arquivos = model.listarArquivos();
        }
	}

	@Override
	public String retornar() {
        this.modoEdicao = false;
        arquivos = model.listarArquivos();
        return "arquivo";
	}

}
