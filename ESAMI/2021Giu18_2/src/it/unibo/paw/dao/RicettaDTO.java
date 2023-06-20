package it.unibo.paw.dao;

import java.io.Serializable;

public class RicettaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private String nome;
	private int tempoPreparazione;
	private String livelloDifficolta;
	private int calorie;

	// -------------------------------------

	public RicettaDTO() {
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTempoPreparazione() {
		return tempoPreparazione;
	}

	public void setTempoPreparazione(int tempoPreparazione) {
		this.tempoPreparazione = tempoPreparazione;
	}

	public String getLivelloDifficolta() {
		return livelloDifficolta;
	}

	public void setLivelloDifficolta(String livelloDifficolta) {
		this.livelloDifficolta = livelloDifficolta;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

}
