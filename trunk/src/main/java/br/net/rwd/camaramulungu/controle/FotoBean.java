package br.net.rwd.camaramulungu.controle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;

import br.net.rwd.camaramulungu.entidade.Foto;
import br.net.rwd.camaramulungu.servico.FotoServico;
import br.net.rwd.camaramulungu.util.Criptografia;
import br.net.rwd.camaramulungu.util.FileParaBytes;
import br.net.rwd.camaramulungu.util.Redimensiona;

@ManagedBean(name = "fotoBean")
@ViewScoped
public class FotoBean extends UtilBean implements CrudBeans<Object> {

	@ManagedProperty("#{fotoServico}")
	private FotoServico model;

	private Integer fot_cod;
	private String fot_descricao;
	private String fot_foto;
	private List<Foto> fotos;

	private Foto foto;
	private boolean modoEdicao;
	//
	
	private static ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	private static final String PATH = extContext.getRealPath("/uploads/fotos/");
	private byte[] bytesFoto;
	String nomeImagem = null;
	String nomeArquivo = null;
	File arquivo = null;
	private String mensagemUpload = null;
	
	/* ------------------------------------------------- */

	public FotoServico getModel() {
		return model;
	}

	public void setModel(FotoServico model) {
		this.model = model;
	}

	public Integer getFot_cod() {
		return fot_cod;
	}

	public void setFot_cod(Integer fot_cod) {
		this.fot_cod = fot_cod;
	}

	public String getFot_descricao() {
		return fot_descricao;
	}

	public void setFot_descricao(String fot_descricao) {
		this.fot_descricao = fot_descricao;
	}

	public String getFot_foto() {
		return fot_foto;
	}

	public void setFot_foto(String fot_foto) {
		this.fot_foto = fot_foto;
	}

	public Foto getFoto() {
		if (foto == null) {
			foto = new Foto();
		}
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public List<Foto> getFotos() {
		fotos = model.listarFotos();
		return fotos;
	}

	public byte[] getBytesFoto() {
		return bytesFoto;
	}

	public void setBytesFoto(byte[] bytesFoto) {
		this.bytesFoto = bytesFoto;
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
		this.foto = new Foto();
		this.modoEdicao = true;
		bytesFoto = null;
		mensagemUpload = null;
	}

	@Override
	public void salvar() {
		if (bytesFoto == null)
			addInfoMensagem("� preciso carregar uma foto antes de salvar!");
		
		if (foto.getFot_cod() == null || foto.getFot_cod().intValue() == 0) {
			
			if (salvaArquivo()) {
				foto = model.incluirFoto(foto);
				foto = new Foto();
				bytesFoto = null;
				addInfoMensagem("Foto incluida com sucesso.");
				retornar();
			} else {
				addErroMensagem("Cadastro da foto n�o realizado!");
			}

		} else {
			
			File arquivoAnterior = new File(PATH);
			if (Criptografia.criptografarMD5(nomeArquivo).concat(".jpg") != foto.getFot_foto()) {
				// exclui o arquivo existente
				if (arquivoAnterior.exists())
					arquivoAnterior.delete();
				if (!salvaArquivo())
					addErroMensagem("Altera��o da imagem n�o realizada!");
			}
			model.alterarFoto(foto);
			bytesFoto = null;
			addInfoMensagem("Cadastro da foto alterado com sucesso.");
			retornar();
			
		}
	}

	@Override
	public void atualizar() {
		atualizarFoto();
		this.modoEdicao = true;
		mensagemUpload = null;
	}

	@Override
	public void excluir() {
		File arquivo = new File(PATH + File.separator + foto.getFot_foto());
		if (arquivo.exists())
			arquivo.delete();

		model.excluirFoto(foto);
		addInfoMensagem("Foto excluida com sucesso.");
		retornar();
	}

	@Override
	public void filtrar(AjaxBehaviorEvent event) {
	}

	@Override
	public String retornar() {
		fotos = model.listarFotos();
		this.modoEdicao = false;
		bytesFoto = null;
		return "foto";
	}

	/* ------------------------------------------------- */
	
	public void atualizarFoto() {
		nomeImagem = foto.getFot_foto();
		arquivo = new File(PATH + File.separator +  nomeImagem);
		if(arquivo.exists()) 
		bytesFoto = FileParaBytes.getFileBytes(arquivo);
		else
			addErroMensagem("O arquivo da foto selecionada n�o foi encontrado! Carregue uma nova foto.");
	}

	public void handleFileUpload(FileUploadEvent event) {
		nomeArquivo = event.getFile().getFileName();
		String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf('.')+1);
		nomeImagem = Criptografia.criptografarMD5(nomeArquivo).concat(".").concat(extensao);
		arquivo = new File(PATH + File.separator + nomeImagem);
		bytesFoto = Redimensiona.novaLargura(event.getFile().getContents(),640,extensao);
		
		mensagemUpload = "<p style='color:#3C82B4;font-weight:bold;background-color:#E2ECFB;height:auto;width:auto;padding:5px;'>A foto " + event.getFile().getFileName() + " foi carregada.\nUse o bot�o salvar para completar a opera��o!</p>";
		
		if (arquivo.exists())
			addAvisoMensagem("J� existe uma foto com mesmo nome, se continuar, a foto atual ser� substitu�da.");
	}

	boolean salvaArquivo() {
		boolean retorno = false;
		// se a pasta nao existir cria
		File pasta = new File(PATH);
		if (!pasta.exists())
			pasta.mkdirs();

		// se o arquivo ja existe exclui
		if (arquivo.exists()) {
			arquivo.delete();
			addAvisoMensagem("O arquivo da foto existente foi excluido.");
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
			byte[] buffer = new byte[bytesFoto.length];
			int bulk;
			InputStream inputStream = new ByteArrayInputStream(bytesFoto);
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();

				// nome da foto no bd
				foto.setFot_foto(nomeImagem);
			}

			fileOutputStream.close();
			inputStream.close();
			addInfoMensagem("O arquivo arquivo da foto foi enviado.");
			retorno = true;
		} catch (IOException e) {
			e.printStackTrace();
			addErroMensagem("O arquivo arquivo da foto n�o foi enviado, tente novamente!");
			retorno = false;
		}
		return retorno;
	}
    
	public List<Foto> getGaleriaFotos() {
		List<Foto> lista = model.listarFotos();
		if (lista.isEmpty())
			return null;
		else
			return lista;
	}
	
}
