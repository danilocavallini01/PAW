package it.unibo.paw.hibernate;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

public class OggettoDigitale implements Serializable{
	static final long serialVersionUID = 1L;
	
	private int id;
	private int codiceOggetto;
	private String nome;
	private String formato;
	private Date dataDigitalizzazione;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCodiceOggetto() {
		return codiceOggetto;
	}
	public void setCodiceOggetto(int codiceOggetto) {
		this.codiceOggetto = codiceOggetto;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public Date getDataDigitalizzazione() {
		return dataDigitalizzazione;
	}
	public void setDataDigitalizzazione(Date dataDigitalizzazione) {
		this.dataDigitalizzazione = dataDigitalizzazione;
	}
	

}
