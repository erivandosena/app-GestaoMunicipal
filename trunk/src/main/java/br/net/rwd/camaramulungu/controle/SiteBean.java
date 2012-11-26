package br.net.rwd.camaramulungu.controle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;

import br.net.rwd.camaramulungu.entidade.Site;
import br.net.rwd.camaramulungu.servico.SiteServico;
import br.net.rwd.camaramulungu.util.FileParaBytes;
import br.net.rwd.camaramulungu.util.Redimensiona;

@ManagedBean(name = "siteBean")
@ViewScoped
public class SiteBean extends UtilBean implements Serializable, CrudBeans<Object> {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{siteServico}")
	private SiteServico model;
	private Site site;
	private List<Site> sites;
	private boolean modoEdicao;

	/* ------------------------------------------------- */

	private Integer web_cod;
	private String web_titulo;
	private String web_descricao;
	private String web_slogan;
	private String web_proprietario;
	private String web_endereco;
	private String web_numero;
	private String web_bairro;
	private String web_cep;
	private String web_estado;
	private String web_cidade;
	private String web_telefone;
	private String web_email;
	private String web_site;
	private String web_orkut;
	private String web_twitter;
	private String web_facebook;
	private String web_flickr;
	private String web_linkedin;
	private String web_myspace;
	private String web_vimeo;
	private String web_youtube;
	private String web_blog;
	private String web_localizacao;
	private String web_messenger;
	private String web_skype;
	private byte[] web_img_padrao;
	private String web_img_mime;
	private String web_img_nome;

	/* ------------------------------------------------- */
	
	private static ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
	private static final String PATH = extContext.getRealPath("/resources/images/");
	private byte[] bytesImagem;
	private String mensagemUpload = null;
	String nomeArquivo = null;
	File arquivo = null;

	public SiteServico getModel() {
		return model;
	}

	public void setModel(SiteServico model) {
		this.model = model;
	}

	public Site getSite() {
		if (site == null) {
			site = new Site();
		}
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public List<Site> getSites() {
		if (sites == null) {
			sites = model.listarSite();
		}
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}
	
	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}
	
	/* ------------------------------------------------- */

	public Integer getWeb_cod() {
		return web_cod;
	}

	public void setWeb_cod(Integer web_cod) {
		this.web_cod = web_cod;
	}

	public String getWeb_titulo() {
		return web_titulo;
	}

	public void setWeb_titulo(String web_titulo) {
		this.web_titulo = web_titulo;
	}

	public String getWeb_descricao() {
		return web_descricao;
	}

	public void setWeb_descricao(String web_descricao) {
		this.web_descricao = web_descricao;
	}

	public String getWeb_slogan() {
		return web_slogan;
	}

	public void setWeb_slogan(String web_slogan) {
		this.web_slogan = web_slogan;
	}

	public String getWeb_proprietario() {
		return web_proprietario;
	}

	public void setWeb_proprietario(String web_proprietario) {
		this.web_proprietario = web_proprietario;
	}

	public String getWeb_endereco() {
		return web_endereco;
	}

	public void setWeb_endereco(String web_endereco) {
		this.web_endereco = web_endereco;
	}

	public String getWeb_numero() {
		return web_numero;
	}

	public void setWeb_numero(String web_numero) {
		this.web_numero = web_numero;
	}

	public String getWeb_bairro() {
		return web_bairro;
	}

	public void setWeb_bairro(String web_bairro) {
		this.web_bairro = web_bairro;
	}

	public String getWeb_cep() {
		return web_cep;
	}

	public void setWeb_cep(String web_cep) {
		this.web_cep = web_cep;
	}

	public String getWeb_estado() {
		return web_estado;
	}

	public void setWeb_estado(String web_estado) {
		this.web_estado = web_estado;
	}

	public String getWeb_cidade() {
		return web_cidade;
	}

	public void setWeb_cidade(String web_cidade) {
		this.web_cidade = web_cidade;
	}

	public String getWeb_telefone() {
		return web_telefone;
	}

	public void setWeb_telefone(String web_telefone) {
		this.web_telefone = web_telefone;
	}

	public String getWeb_email() {
		return web_email;
	}

	public void setWeb_email(String web_email) {
		this.web_email = web_email;
	}

	public String getWeb_site() {
		return web_site;
	}

	public void setWeb_site(String web_site) {
		this.web_site = web_site;
	}

	public String getWeb_orkut() {
		return web_orkut;
	}

	public void setWeb_orkut(String web_orkut) {
		this.web_orkut = web_orkut;
	}

	public String getWeb_twitter() {
		return web_twitter;
	}

	public void setWeb_twitter(String web_twitter) {
		this.web_twitter = web_twitter;
	}

	public String getWeb_facebook() {
		return web_facebook;
	}

	public void setWeb_facebook(String web_facebook) {
		this.web_facebook = web_facebook;
	}

	public String getWeb_flickr() {
		return web_flickr;
	}

	public void setWeb_flickr(String web_flickr) {
		this.web_flickr = web_flickr;
	}

	public String getWeb_linkedin() {
		return web_linkedin;
	}

	public void setWeb_linkedin(String web_linkedin) {
		this.web_linkedin = web_linkedin;
	}

	public String getWeb_myspace() {
		return web_myspace;
	}

	public void setWeb_myspace(String web_myspace) {
		this.web_myspace = web_myspace;
	}

	public String getWeb_vimeo() {
		return web_vimeo;
	}

	public void setWeb_vimeo(String web_vimeo) {
		this.web_vimeo = web_vimeo;
	}

	public String getWeb_youtube() {
		return web_youtube;
	}

	public void setWeb_youtube(String web_youtube) {
		this.web_youtube = web_youtube;
	}

	public String getWeb_blog() {
		return web_blog;
	}

	public void setWeb_blog(String web_blog) {
		this.web_blog = web_blog;
	}

	public String getWeb_localizacao() {
		return web_localizacao;
	}

	public void setWeb_localizacao(String web_localizacao) {
		this.web_localizacao = web_localizacao;
	}

	public String getWeb_messenger() {
		return web_messenger;
	}

	public void setWeb_messenger(String web_messenger) {
		this.web_messenger = web_messenger;
	}

	public String getWeb_skype() {
		return web_skype;
	}

	public void setWeb_skype(String web_skype) {
		this.web_skype = web_skype;
	}

	public byte[] getWeb_img_padrao() {
		return web_img_padrao;
	}

	public void setWeb_img_padrao(byte[] web_img_padrao) {
		this.web_img_padrao = web_img_padrao;
	}

	public String getWeb_img_mime() {
		return web_img_mime;
	}

	public void setWeb_img_mime(String web_img_mime) {
		this.web_img_mime = web_img_mime;
	}

	public String getWeb_img_nome() {
		return web_img_nome;
	}

	public void setWeb_img_nome(String web_img_nome) {
		this.web_img_nome = web_img_nome;
	}
	
	public byte[] getBytesImagem() {
		return bytesImagem;
	}

	public void setBytesImagem(byte[] bytesImagem) {
		this.bytesImagem = bytesImagem;
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
        this.site = new Site();
        this.modoEdicao = true;
	}

	@Override
	public void salvar() {
        if (site.getWeb_cod() == null || site.getWeb_cod().intValue() == 0) {
        	//inclui a imagem upada
        	salvarImagem();
            site = model.incluirSite(site);
            site = new Site();
            addInfoMensagem("Site criado com sucesso.");
            retornar();
        } else {
        	//inclui a imagem upada
        	salvarImagem();
            model.alterarSite(site);
            addInfoMensagem("Site alterado com sucesso.");
            retornar();
        }
	}

	@Override
	public void atualizar() {
		atualizarImagem();
		this.modoEdicao = true;
	}

	@Override
	public void excluir() {
		model.excluirSite(site);
		retornar();
	}

	@Override
	public void filtrar(AjaxBehaviorEvent event) {
        if (web_titulo != null && !web_titulo.isEmpty()) {
            sites = model.listarSite(web_titulo);
        } else {
            sites = model.listarSite();
        }
	}

	@Override
	public String retornar() {
        this.modoEdicao = false;
        sites = model.listarSite();
        return "index";
	}
	
	/* ------------------------------------------------- */
	
	public Site getCarregarDados() {
		List<Site> lista = model.listarSite();
		if (lista.isEmpty())
			return null;
		else
			return (Site) lista.set(0, null);
	}
	
	public String getSaldacao() {
		String texto = null;
		Calendar DataToda = Calendar.getInstance();
		int HoraAtual = DataToda.get(Calendar.HOUR_OF_DAY);
		switch (HoraAtual) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			texto = "Bom dia!";
			break;
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
			texto = "Boa tarde!";
			break;
		default:
			texto = "Boa noite!";
			break;
		}
		return texto;
	}
	
	/* ----------------------UPLOAD--------------------- */

	public void handleFileUpload(FileUploadEvent event) {
		nomeArquivo = "logo.".concat( event.getFile().getFileName() .substring( event.getFile().getFileName() .lastIndexOf('.') + 1));
		arquivo = new File(PATH + File.separator +nomeArquivo);
		bytesImagem = Redimensiona.novaLargura(event.getFile().getContents(),600);
		
		mensagemUpload = "<p style='color:#3C82B4;font-weight:bold;background-color:#E2ECFB;height:auto;width:auto;padding:5px;'>O arquivo " + event.getFile().getFileName() + " foi carregado.\nUse o bot�o salvar para completar a opera��o!</p>";
		
		if (new File(arquivo.getPath()+ File.separator + nomeArquivo).exists())
			addAvisoMensagem("J� existe uma imagem com mesmo nome, se continuar, a imagem atual ser� substitu�da.");
	}
	
	private void salvarImagem() {
		if (bytesImagem != null) {
		if (site.getWeb_img_nome() == null) {
			if (salvaArquivo()) {
				bytesImagem = null;
				addInfoMensagem("Imagem inclu�da com sucesso.");
			} else {
				addErroMensagem("Inclus�o de imagem n�o realizada!");
			}
		} else {

			File arquivoAnterior = new File(PATH + File.separator + site.getWeb_img_nome());
			if (nomeArquivo != site.getWeb_img_nome()) {
				// exclui o arquivo existente
				if (arquivoAnterior.exists())
					arquivoAnterior.delete();

				if (!salvaArquivo()) 
					addErroMensagem("Altera��o da imagem n�o realizada!");
			}
			bytesImagem = null;
		}
		}
	}
	
	private boolean salvaArquivo() {
		boolean retorno = false;
		// se a pasta n�o existir cria
		File pasta = new File(PATH);
		if (!pasta.exists())
			pasta.mkdirs();

		// se o arquivo ja existe exclui
		if (arquivo.exists()) {
			arquivo.delete();
			addAvisoMensagem("O arquivo da imagem existente foi exclu�do.");
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
			byte[] buffer = new byte[bytesImagem.length];
			int bulk;
			InputStream inputStream = new ByteArrayInputStream(bytesImagem);
			while (true) {
				bulk = inputStream.read(buffer);
				if (bulk < 0) {
					break;
				}
				fileOutputStream.write(buffer, 0, bulk);
				fileOutputStream.flush();

				// faz outras coisas aqui
				site.setWeb_img_nome(arquivo.getName());
			}

			fileOutputStream.close();
			inputStream.close();
			addInfoMensagem("O arquivo da imagem foi enviado.");
			retorno = true;
		} catch (IOException e) {
			e.printStackTrace();
			addErroMensagem("O arquivo da imagem n�o foi enviado, tente novamente!");
			retorno = false;
		}
		return retorno;
	}
	
	private void atualizarImagem() {
		nomeArquivo = site.getWeb_img_nome();
		arquivo = new File(PATH + File.separator + site.getWeb_img_nome());
		if(arquivo.exists()) 
		bytesImagem = FileParaBytes.getFileBytes(arquivo);
		else
			addErroMensagem("O arquivo de imagem da logomarca n�o foi encontrado! Carregue uma nova imagem.");
	}
	
	public void excluirImagem() {
		File arquivo = new File(PATH + File.separator + site.getWeb_img_nome());
		if (arquivo.exists())
			arquivo.delete();
		site.setWeb_img_nome(null);
		model.alterarSite(site);
		addInfoMensagem("Imagem exclu�da com sucesso.");
		retornar();
	}
	
	/* ----------------------UPLOAD--------------------- */
}
