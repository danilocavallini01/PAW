package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.Objects;

public class Accertamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String codiceAccertamento;
	private String nome;
	private String descrizione;

	private TipoAccertamento tipoAccertamento;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceAccertamento() {
		return codiceAccertamento;
	}

	public void setCodiceAccertamento(String codiceAccertamento) {
		this.codiceAccertamento = codiceAccertamento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public TipoAccertamento getTipoAccertamento() {
		return tipoAccertamento;
	}

	public void setTipoAccertamento(TipoAccertamento tipoAccertamento) {
		this.tipoAccertamento = tipoAccertamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(codiceAccertamento, descrizione, id, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accertamento other = (Accertamento) obj;
		return Objects.equals(codiceAccertamento, other.codiceAccertamento)
				&& Objects.equals(descrizione, other.descrizione) && id == other.id && Objects.equals(nome, other.nome);
	}

}
