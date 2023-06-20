package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StadioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private int Codice;
	private String Nome;
	private String Citta;
	private List<PartitaDTO> partite;

	// -------------------------------------

	public StadioDTO() {
		this.loaded = false;
		this.partite = new ArrayList<PartitaDTO>();
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

	public int getCodice() {
		return Codice;
	}

	public void setCodice(int codice) {
		Codice = codice;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public String getCitta() {
		return Citta;
	}

	public void setCitta(String citta) {
		Citta = citta;
	}

	public List<PartitaDTO> getPartite() {
		return partite;
	}

	public void setPartite(List<PartitaDTO> partite) {
		this.partite = partite;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Citta, Codice, Nome, id, loaded, partite);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StadioDTO other = (StadioDTO) obj;
		return Objects.equals(Citta, other.Citta) && Codice == other.Codice && Objects.equals(Nome, other.Nome)
				&& id == other.id && loaded == other.loaded && Objects.equals(partite, other.partite);
	}

}
