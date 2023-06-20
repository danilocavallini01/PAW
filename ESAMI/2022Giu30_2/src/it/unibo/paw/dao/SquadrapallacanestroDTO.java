package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.List;

public class SquadrapallacanestroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private String nome;
	private String torneo;
	private String allenatore;


	// -------------------------------------

	public SquadrapallacanestroDTO() {
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

	public String getTorneo() {
		return torneo;
	}

	public void setTorneo(String torneo) {
		this.torneo = torneo;
	}

	public String getAllenatore() {
		return allenatore;
	}

	public void setAllenatore(String allenatore) {
		this.allenatore = allenatore;
	}

}
