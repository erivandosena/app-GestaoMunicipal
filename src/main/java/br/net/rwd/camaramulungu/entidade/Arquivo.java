package br.net.rwd.camaramulungu.entidade;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "arquivos")
public class Arquivo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer arq_cod;
	private String arq_nome;
	private String arq_local;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arq_cod == null) ? 0 : arq_cod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arquivo other = (Arquivo) obj;
		if (arq_cod == null) {
			if (other.arq_cod != null)
				return false;
		} else if (!arq_cod.equals(other.arq_cod))
			return false;
		return true;
	}

}
