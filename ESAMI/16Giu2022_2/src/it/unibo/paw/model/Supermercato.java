package it.unibo.paw.model;

import java.util.List;

public class Supermercato {
	private int codiceSuper;
	private String nome;
	private int ratingGradimento;
	private List<ProdottoOfferto> prodotti;

	public int getCodiceSuper() {
		return codiceSuper;
	}

	public void setCodiceSuper(int codiceSuper) {
		this.codiceSuper = codiceSuper;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getRatingGradimento() {
		return ratingGradimento;
	}

	public void setRatingGradimento(int ratingGradimento) {
		this.ratingGradimento = ratingGradimento;
	}

	public List<ProdottoOfferto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<ProdottoOfferto> prodotti) {
		this.prodotti = prodotti;
	}

}
