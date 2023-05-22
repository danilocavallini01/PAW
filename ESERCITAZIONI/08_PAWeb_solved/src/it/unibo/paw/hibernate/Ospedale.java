package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Ospedale implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int ospId;
	private int codiceOsp;
	private String nome;
	private String citta;
	private String indirizzo;

	private Set<TipoAccertamento> tipiAccertamento = new HashSet<TipoAccertamento>(0);

	public Ospedale() {}

	public int getOspId() {
		return ospId;
	}

	public void setOspId(int ospId) {
		this.ospId = ospId;
	}

	public int getCodiceOsp() {
		return codiceOsp;
	}

	public void setCodiceOsp(int codiceOsp) {
		this.codiceOsp = codiceOsp;
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

	@Override
	public String toString() {
		return "Ospedale [id=" + ospId + ", codice=" + codiceOsp + ", nome=" + nome
				+ ", città=" + citta + ", indirizzo=" + indirizzo
				+ ", tipi accertamento=" + tipiAccertamento + "]";
	}

}
