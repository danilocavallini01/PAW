package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Passeggero implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int codPasseggero;
	private String nome;
	private String cognome;
	private String numPassaporto;

	private Set<VoloAeroportoBo> voliAeroportoBo;

	public int getId() {
		return id;
	}

	public Passeggero() {
		super();
		this.voliAeroportoBo = new HashSet<VoloAeroportoBo>();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodPasseggero() {
		return codPasseggero;
	}

	public void setCodPasseggero(int codPasseggero) {
		this.codPasseggero = codPasseggero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNumPassaporto() {
		return numPassaporto;
	}

	public void setNumPassaporto(String numPassaporto) {
		this.numPassaporto = numPassaporto;
	}

	public Set<VoloAeroportoBo> getVoliAeroportoBo() {
		return voliAeroportoBo;
	}

	public void setVoliAeroportoBo(Set<VoloAeroportoBo> voliAeroportoBo) {
		this.voliAeroportoBo = voliAeroportoBo;
	}

}
