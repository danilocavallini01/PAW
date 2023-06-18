package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class VoloAeroportoBo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int codVolo;
	private String compagniaAerea;
	private String localitaDestinazione;
	private Date dataPartenza;
	private Time orarioPartenza;

	private Set<Passeggero> passeggeri;

	public VoloAeroportoBo() {
		super();
		this.passeggeri = new HashSet<Passeggero>();
	}

	public Set<Passeggero> getPasseggeri() {
		return passeggeri;
	}

	public void setPasseggeri(Set<Passeggero> passeggeri) {
		this.passeggeri = passeggeri;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodVolo() {
		return codVolo;
	}

	public void setCodVolo(int codVolo) {
		this.codVolo = codVolo;
	}

	public String getCompagniaAerea() {
		return compagniaAerea;
	}

	public void setCompagniaAerea(String compagniaAerea) {
		this.compagniaAerea = compagniaAerea;
	}

	public String getLocalitaDestinazione() {
		return localitaDestinazione;
	}

	public void setLocalitaDestinazione(String localitàDestinazione) {
		this.localitaDestinazione = localitàDestinazione;
	}

	public Date getDataPartenza() {
		return dataPartenza;
	}

	public void setDataPartenza(Date dataPartenza) {
		this.dataPartenza = dataPartenza;
	}

	public Time getOrarioPartenza() {
		return orarioPartenza;
	}

	public void setOrarioPartenza(Time orarioPartenza) {
		this.orarioPartenza = orarioPartenza;
	}

}
