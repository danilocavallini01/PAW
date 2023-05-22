package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class ArchivioFisico implements Serializable{
	static final long serialVersionUID = 1L;
	
	private int id;
	private int codiceArchivio;
	private String nome;
	private String descrizione;
	private Date dataCreazione;
	
	private Set<MaterialeFisico> materiali;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodiceArchivio() {
		return codiceArchivio;
	}

	public void setCodiceArchivio(int codiceArchivio) {
		this.codiceArchivio = codiceArchivio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Set<MaterialeFisico> getMateriali() {
		return materiali;
	}

	public void setMateriali(Set<MaterialeFisico> materiali) {
		this.materiali = materiali;
	}
}
