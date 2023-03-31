package it.unibo.paw.model;

import java.sql.Date;

public class PrenotazioneRistorante {
	
    private int id;

    private String cognome;
	private Date data;
    private int numeroPersone;
    private String cellulare;
    private int idTavolo;
    
	public PrenotazioneRistorante(String cognome, Date data, int numeroPersone, String cellulare, int idTavolo) {
		super();
		this.cognome = cognome;
		this.data = data;
		this.numeroPersone = numeroPersone;
		this.cellulare = cellulare;
		this.idTavolo = idTavolo;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getNumeroPersone() {
		return numeroPersone;
	}
	public void setNumeroPersone(int numeroPersone) {
		this.numeroPersone = numeroPersone;
	}
	public String getCellulare() {
		return cellulare;
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public int getIdTavolo() {
		return idTavolo;
	}
	public void setIdTavolo(int idTavolo) {
		this.idTavolo = idTavolo;
	}
}
