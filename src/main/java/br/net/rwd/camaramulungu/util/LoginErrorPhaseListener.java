package br.net.rwd.camaramulungu.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;

import br.net.rwd.camaramulungu.controle.UtilBean;

/**
 * PhaseListener utilizado para capturar excecoes de autenticacao
 *
 */

public class LoginErrorPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -1216620620302322995L;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@SuppressWarnings("unchecked")
	@Override
	public void beforePhase(PhaseEvent arg0) {
		Exception dadosIncorretosException = (Exception) UtilBean.getSessionMap().get(WebAttributes.AUTHENTICATION_EXCEPTION);
		if(dadosIncorretosException instanceof BadCredentialsException) {
			UtilBean.getSessionMap().put(WebAttributes.AUTHENTICATION_EXCEPTION, null);
			UtilBean.exibirMensagemErro("Dados incorretos!");
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
