package it.unibo.paw.model;

import java.io.Serializable;

public class Acquisto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int codiceAcquisto;
	private float importo;
	private String nomeAcquirente;
	private String cognomeAcquirente;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodiceAcquisto() {
		return codiceAcquisto;
	}

	public void setCodiceAcquisto(int codiceAcquisto) {
		this.codiceAcquisto = codiceAcquisto;
	}

	public float getImporto() {
		return importo;
	}

	public void setImporto(float importo) {
		this.importo = importo;
	}

	public String getNomeAcquirente() {
		return nomeAcquirente;
	}

	public void setNomeAcquirente(String nomeAcquirente) {
		this.nomeAcquirente = nomeAcquirente;
	}

	public String getCognomeAcquirente() {
		return cognomeAcquirente;
	}

	public void setCognomeAcquirente(String cognomeAcquirente) {
		this.cognomeAcquirente = cognomeAcquirente;
	}

}
