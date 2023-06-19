package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.List;

public class IngredienteDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private String nome;
	private int quantita;

	private List<RicettaDTO> ricette;

	// -------------------------------------

	public IngredienteDTO() {
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

	protected void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public List<RicettaDTO> getRicette() {
		return ricette;
	}

	public void setRicette(List<RicettaDTO> ricette) {
		this.ricette = ricette;
	}

	@Override
	public String toString() {
		return "IngredienteDTO [id=" + id + ", loaded=" + loaded + ", nome=" + nome + ", quantita=" + quantita
				+ ", ricette=" + ricette + "]";
	}
	
	
}
