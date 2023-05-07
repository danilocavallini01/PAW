package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Ospedale implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String codiceOspedale;
	private String nome;
	private String citta;
	private String indirizzo;

	private Set<TipoAccertamento> tipiAccertamento;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceOspedale() {
		return codiceOspedale;
	}

	public void setCodiceOspedale(String codiceOspedale) {
		this.codiceOspedale = codiceOspedale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public Set<TipoAccertamento> getTipiAccertamento() {
		return tipiAccertamento;
	}

	public void setTipiAccertamento(Set<TipoAccertamento> tipiAccertamento) {
		this.tipiAccertamento = tipiAccertamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(citta, codiceOspedale, id, indirizzo, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ospedale other = (Ospedale) obj;
		return Objects.equals(citta, other.citta) && Objects.equals(codiceOspedale, other.codiceOspedale)
				&& id == other.id && Objects.equals(indirizzo, other.indirizzo) && Objects.equals(nome, other.nome);
	}

}
