package br.net.rwd.camaramulungu.controle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;

import br.net.rwd.camaramulungu.entidade.Arquivo;
import br.net.rwd.camaramulungu.servico.ArquivoServico;
import br.net.rwd.camaramulungu.util.Criptografia;
import br.net.rwd.camaramulungu.util.FileParaBytes;

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
	
	private static ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	private static final String PATH = extContext.getRealPath("/uploads/arquivos/");
	private byte[] bytesArquivo;
	private String mensagemUpload = null;
	String nomeArquivo = null;
	File arquivoUp = null;
	
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
	
	public byte[] getBytesArquivo() {
		return bytesArquivo;
	}

	public void setBytesArquivo(byte[] bytesArquivo) {
		this.bytesArquivo = bytesArquivo;
	}
	
	public String getMensagemUpload() {
		return mensagemUpload;
	}

	public void setMensagemUpload(String mensagemUpload) {
		this.mensagemUpload = mensagemUpload;
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
        	//inclui o arquivo upado
        	salvarArquivo();
            arquivo = model.incluirArquivo(arquivo);
            arquivo = new Arquivo();
            addInfoMensagem("Arquivo upado com sucesso.");
            retornar();
        } else {
        	//inclui o arquivo upado
        	salvarArquivo();
            model.alterarArquivo(arquivo);
            addInfoMensagem("Arquivo alterado com sucesso.");
            retornar();
        }	
	}

	@Override
	public void atualizar() {
		atualizarArquivo();
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
	
	/* ----------------------UPLOAD--------------------- */

	public void handleFileUpload(FileUploadEvent event) {
		nomeArquivo = Criptografia.criptografarMD5(event.getFile().getFileName()).concat(".").concat( event.getFile().getFileName().substring( event.getFile().getFileName() .lastIndexOf('.') + 1));
		arquivoUp = new File(PATH + File.separator +nomeArquivo);
		bytesArquivo = event.getFile().getContents();
		
		if (new File(arquivoUp.getPath()+ File.separator + nomeArquivo).exists())
			addAvisoMensagem("Já existe um arquivo com mesmo nome, se continuar, o arquivo atual será substituído.");

		mensagemUpload = "<p style='color:#C09853;font-weight:bold;background-color:#FCF8E3;height:15px;width:auto;padding:5px;'>O arquivo " + event.getFile().getFileName() + " foi carregado.\nUse o botão salvar para completar a operação!</p>";
		//addAvisoMensagem("O arquivoUp " + event.getFile().getFileName() + " foi carregado. \nUse o botão salvar para completar a operação!");
	}
	
	private void salvarArquivo() {
		if (bytesArquivo != null) {
		if (arquivo.getArq_local() == null) {
			if (uparArquivo()) {
				bytesArquivo = null;
				addInfoMensagem("Arquivo incluído com sucesso.");
			} else {
				addErroMensagem("Inclusão do arquivo não realizada!");
			}
		} else {

			File arquivoAnterior = new File(PATH + File.separator + arquivo.getArq_local());
			if (nomeArquivo != arquivo.getArq_local()) {
				// exclui o arquivo existente
				if (arquivoAnterior.exists())
					arquivoAnterior.delete();

				if (!uparArquivo()) 
					addErroMensagem("Alteração do arquivo não realizada!");
			}
			bytesArquivo = null;
		}
		}
	}
	
	private boolean uparArquivo() {
		boolean retorno = false;
		// se a pasta não existir cria
		File pasta = new File(PATH);
		if (!pasta.exists())
			pasta.mkdirs();

		// se o arquivo ja existe exclui
		if (arquivoUp.exists()) {
			arquivoUp.delete();
			addAvisoMensagem("O arquivo existente foi excluído.");
		}

		FileOutputStream fileOutputStream = null;
		try {

			fileOutputStream = new FileOutputStream(arquivoUp);
			fileOutputStream.write(bytesArquivo);
			fileOutputStream.flush();
			
			// faz outras coisas aqui
			arquivo.setArq_local(arquivoUp.getName());

			addInfoMensagem("O arquivo foi enviado.");
			retorno = true;
		} catch (IOException e) {
			e.printStackTrace();
			addErroMensagem("O arquivo não foi enviado, tente novamente!");
			retorno = false;
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	private void atualizarArquivo() {
		nomeArquivo = arquivo.getArq_local();
		arquivoUp = new File(PATH + File.separator + arquivo.getArq_local());
		if(arquivoUp.exists()) 
			bytesArquivo = FileParaBytes.getFileBytes(arquivoUp);
		else
			addErroMensagem("O arquivo não foi encontrado! Carregue um novo.");
	}
	
	public void excluirArquivo() {
		File arq = new File(PATH + File.separator + arquivo.getArq_local());
		if (arq.exists())
			arq.delete();
		arquivo.setArq_local(null);
		model.alterarArquivo(arquivo);
		addInfoMensagem("Arquivo excluído com sucesso.");
		retornar();
	}
	
	/* ----------------------UPLOAD--------------------- */

	public List<Arquivo> getArquivosDownload() {
		return model.listarArquivos();
	}
}
