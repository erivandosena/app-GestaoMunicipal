package br.net.rwd.camaramulungu.controle;

import javax.faces.event.AjaxBehaviorEvent;

public interface CrudBeans<T> {

	public void incluir();

	public void salvar();

	public void atualizar();

	public void excluir();

	public void filtrarSite(AjaxBehaviorEvent event);

	public String retornar();
	
}
