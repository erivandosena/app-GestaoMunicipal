package br.net.rwd.camaramulungu.controle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;

import br.net.rwd.camaramulungu.dao.SiteDAO;
import br.net.rwd.camaramulungu.entidade.Site;

@Controller("contatoBean")
@RequestScoped
public class ContatoBean extends UtilBean {

	@Autowired
	private SiteDAO siteDao;

	private static String nome;
	private String email;
	private String assunto;
	private static String mensagem;
	
	private static final String IMAGEM = "/resources/images/logo_camara_mulungu_107x80.jpg";

	@Autowired
	private JavaMailSender enviarEmail;

	public ContatoBean() {

	}

	public void setSiteDao(SiteDAO siteDao) {
		this.siteDao = siteDao;
	}

	public void setEnviarEmail(JavaMailSender enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		ContatoBean.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		ContatoBean.mensagem = mensagem;
	}

	public String enviar() {
		final String EMAIL_SITE = siteDao.obterEntidade(Site.class,"SELECT s FROM Site s").getWeb_email();
		if (assunto.equals("")) assunto = "Assunto não especificado";
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper MMhelper = new MimeMessageHelper(mimeMessage,true, "ISO-8859-1");
				MMhelper.setFrom(new InternetAddress(email, nome));
				MMhelper.setTo(EMAIL_SITE);
				MMhelper.setBcc(email);
				MMhelper.setPriority(Thread.MAX_PRIORITY);
				MMhelper.setSentDate(new Date());
				MMhelper.setSubject(assunto);
				setHTMLContent(MMhelper);
			}
		};

		try {
			this.enviarEmail.send(preparator);
			nome = "";
			email = "";
			assunto = "";
			mensagem = "";
			addInfoMensagem("Enviado com sucesso!");
			return "contato";
		} catch (MailException ex) {
			addInfoMensagem("Erro ao enviar.");
		}
		return null;
	}

	public static void setHTMLContent(MimeMessageHelper msg) throws MessagingException {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

		String html = "</title>"+
            "</head>"+
            "<body lang=PT-BR link=blue vlink=purple>"+
            "<div align=center>"+
              "<table border='0' cellspacing='0' cellpadding='5' width='550'>"+
                "<tr bgcolor='#F0EEF1'>"+
                  "<td width='175'><p><span style='font-family: Arial'><a href='"+request.getRequestURL().toString().replace(request.getRequestURI(),request.getContextPath())+"'>" +
                  "<span style='text-decoration: none; text-underline: none'><img border='0' height='80' src='"+request.getRequestURL().toString().replace(request.getRequestURI(),request.getContextPath())+ IMAGEM +"'/>" +
                  "</span></a></span></p></td>"+
                  "<td width='372'></td>"+
                "</tr>"+
                "<tr>"+
                  "<td></td>"+
                  "<td></td>"+
                  "<td></td>"+
                "</tr>"+
                "<tr>"+
                  "<td colspan='3'><p style='line-height: 13.5pt'><span style='font-size: 9.0pt; font-family: Arial; color: #333333'>Olá! <br />"+
                      "Meu nome é <strong> "+ nome.toUpperCase() +" </strong></span></p>"+
                    "<p style='line-height: 13.5pt'><span style='font-size: 9.0pt; font-family: Arial; color: #333333'> "+ mensagem +" </span></p>"+
                    "<p style='line-height: 13.5pt'><span style='font-size: 8.0pt; font-family: Arial; color: #333333'><i>Mensagem originada do site, o usuário " + nome + " foi registrado com o IP Nº: "+ request.getRemoteHost() +" </i></span></p></td>"+
                "</tr>"+
                "<tr bgcolor='#999999' style='mso-yfti-irow: 3; height: 6.75pt'>"+
                  "<td colspan='3'></td>"+
                "</tr>"+
                "<tr>"+
                  "<td colspan='3'><p align=center style='text-align: center'> <span style='font-size: 8.5pt; font-family: Arial; color: #666666'> © Produzido por <a href='http://www.rwd.net.br'>RWD</a></span></p></td>"+
                "</tr>"+
              "</table>"+
            "</div>"+
            "</body>"+
            "</html>";
		// HTMLDataSource é uma classe interna
		msg.getMimeMessage().setDataHandler(new DataHandler(new HTMLDataSource(html)));
	}

	static class HTMLDataSource implements DataSource {
		private String html;

		public HTMLDataSource(String htmlString) {
			html = htmlString;
		}

		// String html de retorno em um InputStream.
		// Um novo fluxo deve ser devolvido a cada vez.
		public InputStream getInputStream() throws IOException {
			if (html == null)
				throw new IOException("HTML nulo");
			return new ByteArrayInputStream(html.getBytes());
		}

		public OutputStream getOutputStream() throws IOException {
			throw new IOException("Este DataHandler não pode escrever HTML");
		}

		public String getContentType() {
			return "text/html";
		}

		public String getName() {
			return "JAF text/html dataSource apenas para enviar e-mail";
		}
	}

}
