package br.net.rwd.camaramulungu.controle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;

import br.net.rwd.camaramulungu.entidade.Usuario;
import br.net.rwd.camaramulungu.servico.UsuarioServico;
import br.net.rwd.camaramulungu.util.Criptografia;
import br.net.rwd.camaramulungu.util.EnumPerfilUsuario;

@ManagedBean(name = "usuarioBean")
@ViewScoped
public class UsuarioBean extends UtilBean implements CrudBeans<Object> {

	private static final long serialVersionUID = 1L;
	
	@ManagedProperty("#{usuarioServico}")
	private UsuarioServico model;
	private Usuario usuario;
	private List<Usuario> usuarios;
	private boolean modoEdicao;

	/* ------------------------------------------------- */
	
	private Integer usu_cod;
	private String usu_nome;
	private String usu_email;
	private String usu_senha;
	private String usu_endereco;
	private String usu_numero;
	private String usu_cidade;
	private String usu_cep;
	private String usu_estado;
	private boolean usu_situacao = true;
	private String usu_perfil;
	
	private String confirmaSenha;
	private String loginExistente;
	
	private String senhaExistente;
	
	/* ------------------------------------------------- */
	
	public UsuarioServico getModel() {
		return model;
	}

	public void setModel(UsuarioServico model) {
		this.model = model;
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		if (usuarios == null) {
			usuarios = model.listarUsuarios();
		}
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}
	
	/* ------------------------------------------------- */

	public Integer getUsu_cod() {
		return usu_cod;
	}

	public void setUsu_cod(Integer usu_cod) {
		this.usu_cod = usu_cod;
	}

	public String getUsu_nome() {
		return usu_nome;
	}

	public void setUsu_nome(String usu_nome) {
		this.usu_nome = usu_nome;
	}

	public String getUsu_email() {
		return usu_email;
	}

	public void setUsu_email(String usu_email) {
		this.usu_email = usu_email;
	}

	public String getUsu_senha() {
		return usu_senha;
	}

	public void setUsu_senha(String usu_senha) {
		this.usu_senha = usu_senha;
	}

	public String getUsu_endereco() {
		return usu_endereco;
	}

	public void setUsu_endereco(String usu_endereco) {
		this.usu_endereco = usu_endereco;
	}

	public String getUsu_numero() {
		return usu_numero;
	}

	public void setUsu_numero(String usu_numero) {
		this.usu_numero = usu_numero;
	}

	public String getUsu_cidade() {
		return usu_cidade;
	}

	public void setUsu_cidade(String usu_cidade) {
		this.usu_cidade = usu_cidade;
	}

	public String getUsu_cep() {
		return usu_cep;
	}

	public void setUsu_cep(String usu_cep) {
		this.usu_cep = usu_cep;
	}

	public String getUsu_estado() {
		return usu_estado;
	}

	public void setUsu_estado(String usu_estado) {
		this.usu_estado = usu_estado;
	}
	
	public boolean isUsu_situacao() {
		return usu_situacao;
	}

	public void setUsu_situacao(boolean usu_situacao) {
		this.usu_situacao = usu_situacao;
	}
	
	public String getUsu_perfil() {
		return usu_perfil;
	}

	public void setUsu_perfil(String usu_perfil) {
		this.usu_perfil = usu_perfil;
	}
	
	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
	
	public String getLoginExistente() {
		return loginExistente;
	}
	
	/* ------------------------------------------------- */

	@Override
	public void incluir() {
        this.usuario = new Usuario();
        this.modoEdicao = true;
	}

	@Override
	public void salvar() {
		// senha automatica
		String senhaAuto = getGeraSenha();

		// compara as senhas digitadas
		if (!usuario.getUsu_senha().equals(this.getConfirmaSenha())) {
			addAvisoMensagem("Senhas não conferem.");
		} else {
			// remove espacos e mantem o e-mail em minusculo
			String emailUsuario = usuario.getUsu_email().trim().toLowerCase();
			usuario.setUsu_email(emailUsuario);

			// criptografia da senha
			if (usuario.getUsu_cod() == null || usuario.getUsu_cod().intValue() == 0) {
				// quando inserindo
				if (usuario.getUsu_senha().trim().isEmpty()) {
					usuario.setUsu_senha(Criptografia.criptografarMD5(senhaAuto));
				} else {
					senhaAuto = usuario.getUsu_senha();
					usuario.setUsu_senha(Criptografia.criptografarMD5(senhaAuto));
				}
			} else {
				// quando atualizando
				if (usuario.getUsu_senha().trim().isEmpty()) {
					// se for administrador gera nova senha
					LoginBean login = new LoginBean();
					if (login.getUsuarioEmSessao() == "ROLE_ADMINISTRADOR") {
						usuario.setUsu_senha(Criptografia.criptografarMD5(senhaAuto));
					} else {
						senhaAuto = "***** (Sem alteração.)";
						usuario.setUsu_senha(senhaExistente);
					}
				} else {
					senhaAuto = usuario.getUsu_senha();
					usuario.setUsu_senha(Criptografia.criptografarMD5(senhaAuto));
				}
			}

			// Verifica se é um insert
			if (usuario.getUsu_cod() == null || usuario.getUsu_cod().intValue() == 0) {
				/*---- NOVO ----*/
				// verifica se ja existe
				if (model.selecionarUsuarioExistente(emailUsuario)) {
					addErroMensagem("Usuário existente! Informe outro e-mail.");
				} else {

					if (this.usuario.getUsu_perfil().toString() == "[]") {
						addErroMensagem("Selecione o perfil.");
					} else {			
						// inclui
						usuario = model.incluirUsuario(usuario);

						// envia email
//						envia(usuario.getUsu_nome(), usuario.getUsu_email(), senhaAuto,"Seu cadastro para acesso foi criado");

						usuario = new Usuario();
						addInfoMensagem("Usuário criado com sucesso.");
						retornar();
					}
				}
			} else {
				/*---- ATUALIZA ----*/
				// verifica se o email foi alterado
				if (!usuario.getUsu_email().equals(loginExistente)) {
					// verifica se ja existe
					if (model.selecionarUsuarioExistente(emailUsuario)) {
						addErroMensagem("Usuário existente! Informe outro e-mail.");
					} else {
						// envia email
//						envia(usuario.getUsu_nome(), usuario.getUsu_email(), senhaAuto, "Seu e-mail do cadastro foi alterado");

						// atualiza com login novo
						model.alterarUsuario(usuario);
						addInfoMensagem("Usuário alterado com sucesso.");
						retornar();
					}
				} else {
					// envia email
//					envia(usuario.getUsu_nome(), usuario.getUsu_email(), senhaAuto, "Seu cadastro foi alterado");
					
					// atualiza com login antigo
					model.alterarUsuario(usuario);
					addInfoMensagem("Usuário alterado com sucesso.");
					retornar();
				}
			}
		}

	}

	@Override
	public void atualizar() {
		loginExistente = usuario.getUsu_email();
		senhaExistente = usuario.getUsu_senha();
		this.modoEdicao = true;
	}

	@Override
	public void excluir() {
		if (usuario.getUsu_nome().equals("Administrador")) {
			addInfoMensagem("Operação não permitida.");
		} else {
			model.excluirUsuario(usuario);
			retornar();
		}
	}

	@Override
	public void filtrar(AjaxBehaviorEvent event) {
        if (usu_nome != null && !usu_nome.isEmpty()) {
            usuarios = model.listarLikeUsuario("%"+usu_nome+"%");
        } else {
            usuarios = model.listarUsuarios();
        }
	}

	@Override
	public String retornar() {
        this.modoEdicao = false;
        usuarios = model.listarUsuarios();
        return "usuario";
	}
	
	public Map<EnumPerfilUsuario, String> getPerfis() {
		Map<EnumPerfilUsuario, String> mapParam = new HashMap<EnumPerfilUsuario, String>();
		for (EnumPerfilUsuario type : EnumPerfilUsuario.values()) {
			mapParam.put(type, type.name());
		}
		return mapParam;
	} 

	private String getGeraSenha() {
		return String.format("%05x",  (int)(Math.random() * 999999L));
	}
	
	/* ----------------- E-MAIL ---------------------- */
	public String msgHtml(String nome, String email, String senha) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String html = "</title></head>"
			+ "<body lang=PT-BR link=blue vlink=purple>"
			+ "<div align=center>"
			+ "<table border='0' cellspacing='0' cellpadding='5' width='550'>"
			+ "<tr><td></td><td></td><td></td></tr><tr>"
			+ "<td bgcolor='#fff' valign='top' style='border:0;'>"
			+ "<table width='100%' align='left' border='0' cellspacing='10' cellpadding='0'><tr>"
			+ "<td colspan='2' align='left'><p align='left'>Parabéns! <strong>"
			+ nome.toUpperCase()
			+ "</strong></p><p align='justify'> "
			+ "Segue seus dados de acesso da área administrativa do site, para fazer seu login utilize as informações abaixo.</p></td>"
			+ "</tr>"
			+ "</table>"
			+ "</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td style='border:0'></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td bgcolor='#fff' valign='top' style='border:0'>"
			+ "<table width='100%' align='left' border='0' cellspacing='10' cellpadding='0'>"
			+ "<tr>"
			+ "<td align='left' width='150'>Usuário:</td>"
			+ "<td align='left' style='color:#000000;'>"
			+ email
			+ "</td>"
			+ "</tr>"
			+ "<td align='left' width='150'>Senha:</td>"
			+ "<td align='left'>"
			+ senha
			+ "</td>"
			+ "</tr>"
			+ "</table>"
			+ "</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan='3'>"
			+ "<p style='line-height: 13.5pt'><span style='font-size: 8.0pt; font-family: Arial; color: #333333'><i>IP Nº: "
			+ request.getRemoteHost()
			+ " </i></span></p></td>"
			+ "</tr>"
			+ "<tr bgcolor='#999999' style='mso-yfti-irow: 3; height: 6.75pt'>"
			+ "<td colspan='3'></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td colspan='3'><p align=center style='text-align: center'> <span style='font-size: 8.5pt; font-family: Arial; color: #666666'> © Produzido por <a href='http://www.rwd.net.br'>RWD</a></span></p></td>"
			+ "</tr></table></div></body></html>";
		return html;
	}

}
