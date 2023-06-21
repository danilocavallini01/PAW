package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.List;

public class GiocatoreDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private String codiceFiscale;
	private String cognome;
	private String nome;
	private int eta;
	private List<SquadraPallacanestroDTO> squadre;

	// -------------------------------------

	public GiocatoreDTO() {
		this.loaded = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getEta() {
		return eta;
	}

	public void setEta(int eta) {
		this.eta = eta;
	}

	public List<SquadraPallacanestroDTO> getSquadre() {
		return squadre;
	}

	public void setSquadre(List<SquadraPallacanestroDTO> squadre) {
		this.squadre = squadre;
	}

}
