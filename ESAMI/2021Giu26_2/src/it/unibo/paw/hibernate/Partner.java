package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Partner implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String siglaPartner;
	private String nome;

	private Set<WorkPackage> workPackages;

	public Partner() {
		this.workPackages = new HashSet<WorkPackage>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSiglaPartner() {
		return siglaPartner;
	}

	public void setSiglaPartner(String siglaPartner) {
		this.siglaPartner = siglaPartner;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<WorkPackage> getWorkPackages() {
		return workPackages;
	}

	public void setWorkPackages(Set<WorkPackage> workPackages) {
		this.workPackages = workPackages;
	}

}
