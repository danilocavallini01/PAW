package it.unibo.paw.hibernate;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

public class MaterialeFisico implements Serializable{
	static final long serialVersionUID = 1L;
	
	private int id;
	private int codiceMateriale;
	private String nome;
	private String descrizione;
	private Date dataCreazione;
	
	private Set<OggettoDigitale> oggetti;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getCodiceMateriale() {
		return codiceMateriale;
	}

	public void setCodiceMateriale(int codiceMateriale) {
		this.codiceMateriale = codiceMateriale;
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

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<OggettoDigitale> getOggetti() {
		return oggetti;
	}

	public void setOggetti(Set<OggettoDigitale> oggetti) {
		this.oggetti = oggetti;
	}

	
}
