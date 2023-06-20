package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

public class Progetto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int codiceProgetto;
	private String nomeProgetto;
	private int annoInizio;
	private String durata;

	private Set<WorkPackage> workPackages;

	public Progetto() {
		this.workPackages = new HashSet<WorkPackage>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodiceProgetto() {
		return codiceProgetto;
	}

	public void setCodiceProgetto(int codiceProgetto) {
		this.codiceProgetto = codiceProgetto;
	}

	public String getNomeProgetto() {
		return nomeProgetto;
	}

	public void setNomeProgetto(String nomeProgetto) {
		this.nomeProgetto = nomeProgetto;
	}

	public int getAnnoInizio() {
		return annoInizio;
	}

	public void setAnnoInizio(int annoInizio) {
		this.annoInizio = annoInizio;
	}

	public String getDurata() {
		return durata;
	}

	public void setDurata(String durata) {
		this.durata = durata;
	}

	public Set<WorkPackage> getWorkPackages() {
		return workPackages;
	}

	public void setWorkPackages(Set<WorkPackage> workPackages) {
		this.workPackages = workPackages;
	}

}
