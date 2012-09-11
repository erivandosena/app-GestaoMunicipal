package br.net.rwd.camaramulungu.controle;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.net.rwd.camaramulungu.servico.UsuarioServico;
import br.net.rwd.camaramulungu.util.Criptografia;

@ManagedBean(name = "loginBean")
@RequestScoped
public class LoginBean {
	
	@ManagedProperty("#{usuarioServico}")
	private UsuarioServico model;

	//@ManagedProperty(value = "#{authenticationService}")
	//private UsuarioAuthenticationService authenticationService;

	private String usuario;
	private String senha;

	public UsuarioServico getModel() {
		return model;
	}

	public void setModel(UsuarioServico model) {
		this.model = model;
	}
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String logar() throws IOException, ServletException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check?j_username=" + usuario + "&j_password=" + Criptografia.criptografarMD5(senha));
		dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse());
		FacesContext.getCurrentInstance().responseComplete();
		return null;
	}

	public String getUsuarioLogado() {
		String username = null;
		Object usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (usuarioLogado instanceof UserDetails) {
			username = model.selecionarUsuarioLogin(((UserDetails)usuarioLogado).getUsername()).getUsu_nome().toString();
		} else {
			username = usuarioLogado.toString().replace("anonymousUser","Visitante");
		}
		return username;
	}
	
	public boolean getStatusLogado() {
		Object usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (usuarioLogado instanceof UserDetails) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getUsuarioEmSessao() {
		Object usuarioLogado = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (usuarioLogado instanceof UserDetails) {
			return ((UserDetails)usuarioLogado).getAuthorities().iterator().next().getAuthority().toString();
		} else {
			return "";
		}
	}

}
