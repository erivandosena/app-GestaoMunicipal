package br.net.rwd.camaramulungu.util;

public enum EnumPerfilUsuario {
	ROLE_USUARIO("Usuário"), ROLE_ADMINISTRADOR("Administrador");

	private String role;

	EnumPerfilUsuario(String role) {
		this.role = role;
	}

	public String getPerfilUsuario() {
		return role;
	}

	@Override
	public String toString() {
		return role;
	}

}
