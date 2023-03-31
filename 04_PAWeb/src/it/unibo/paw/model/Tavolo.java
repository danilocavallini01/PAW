package it.unibo.paw.model;

import java.util.HashSet;
import java.util.Set;

public class Tavolo {

	private int id;

	private String numero;
	private int capienza;
	private Set<PrenotazioneRistorante> prenotazioni;	
	
	public Tavolo() {
		this.prenotazioni = new HashSet<PrenotazioneRistorante>();
	}
	
	public Tavolo(String numero, int capienza) {
		this();
		this.numero = numero;
		this.capienza = capienza;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public int getCapienza() {
		return capienza;
	}
	public void setCapienza(int capienza) {
		this.capienza = capienza;
	}
	public Set<PrenotazioneRistorante> getPrenotazioni() {
		return prenotazioni;
	}
	public void setPrenotazioni(Set<PrenotazioneRistorante> prenotazioni) {
		this.prenotazioni = prenotazioni;
	}
}
