package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.List;

public class RistoranteDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String nomeRistorante;
	private String indirizzo;
	private int rating;
	private List<PiattoDTO> piatti;

	private boolean alreadyLoaded;

	public RistoranteDTO() {
		alreadyLoaded = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeRistorante() {
		return nomeRistorante;
	}

	public void setNomeRistorante(String nomeRistorante) {
		this.nomeRistorante = nomeRistorante;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public List<PiattoDTO> getPiatti() {
		return this.piatti;
	}

	public void setPiatti(List<PiattoDTO> piatti) {
		this.piatti = piatti;
	}

	public boolean isAlreadyLoaded() {
		return this.alreadyLoaded;
	}

	public void isAlreadyLoaded(boolean loaded) {
		this.alreadyLoaded = loaded;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RistoranteDTO other = (RistoranteDTO) obj;
		if (nomeRistorante != other.getNomeRistorante())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RistoranteDTO [nomeRistorante=" + nomeRistorante
				+ ", indirizzo=" + indirizzo + ", rating=" + rating + "]";
	}

}
