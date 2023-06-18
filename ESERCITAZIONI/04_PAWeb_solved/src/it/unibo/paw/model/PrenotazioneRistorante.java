package it.unibo.paw.model;

import java.sql.Date;

public class PrenotazioneRistorante {
	
	private int idPrenotazione;
	private String cognomePrenotazione;
	private Date dataPrenotazione;
	private int numeroPersonePrenotazione;
	private String cellularePrenotazione;
	private int idTavoloPrenotazione;
	
	
	
	public PrenotazioneRistorante() {
		
	}

	public PrenotazioneRistorante(int idPrenotazione,
			String cognomePrenotazione, Date dataPrenotazione,
			int numeroPersonePrenotazione, String cellularePrenotazione, int tavolo) {
		super();
		this.idPrenotazione = idPrenotazione;
		this.cognomePrenotazione = cognomePrenotazione;
		this.dataPrenotazione = dataPrenotazione;
		this.numeroPersonePrenotazione = numeroPersonePrenotazione;
		this.cellularePrenotazione = cellularePrenotazione;
		this.idTavoloPrenotazione = tavolo;
	}

	public int getIdPrenotazione() {
		return idPrenotazione;
	}

	public void setIdPrenotazione(int idPrenotazione) {
		this.idPrenotazione = idPrenotazione;
	}

	public String getCognomePrenotazione() {
		return cognomePrenotazione;
	}

	public void setCognomePrenotazione(String cognomePrenotazione) {
		this.cognomePrenotazione = cognomePrenotazione;
	}

	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public void setDataPrenotazione(Date dataPrenotazione) {
		this.dataPrenotazione = dataPrenotazione;
	}

	public int getNumeroPersonePrenotazione() {
		return numeroPersonePrenotazione;
	}

	public void setNumeroPersonePrenotazione(int numeroPersonePrenotazione) {
		this.numeroPersonePrenotazione = numeroPersonePrenotazione;
	}

	public String getCellularePrenotazione() {
		return cellularePrenotazione;
	}

	public void setCellularePrenotazione(String cellularePrenotazione) {
		this.cellularePrenotazione = cellularePrenotazione;
	}
	
	

	public int getIdTavoloPrenotazione() {
		return idTavoloPrenotazione;
	}

	public void setIdTavoloPrenotazione(int idTavoloPrenotazione) {
		this.idTavoloPrenotazione = idTavoloPrenotazione;
	}

	@Override
	public String toString() {
		return "PrenotazioneRistorante [cognomePrenotazione="
				+ cognomePrenotazione + ", dataPrenotazione="
				+ dataPrenotazione + ", numeroPersonePrenotazione="
				+ numeroPersonePrenotazione + ", cellularePrenotazione="
				+ cellularePrenotazione + "]";
	}
	
	
	
	
}
