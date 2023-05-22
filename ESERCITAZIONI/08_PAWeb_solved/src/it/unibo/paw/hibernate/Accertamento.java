package it.unibo.paw.hibernate;

import java.io.Serializable;

public class Accertamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int accId;
	private int codiceAcc;
	private String nome;
	private String descrizione;

	private TipoAccertamento tipoAccertamento;

	public Accertamento() {
	}

	public int getAccId() {
		return accId;
	}

	public void setAccId(int accId) {
		this.accId = accId;
	}

	public int getCodiceAcc() {
		return codiceAcc;
	}

	public void setCodiceAcc(int codiceAcc) {
		this.codiceAcc = codiceAcc;
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

	public TipoAccertamento getTipoAccertamento() {
		return tipoAccertamento;
	}

	public void setTipoAccertamento(TipoAccertamento tipoAccertamento) {
		this.tipoAccertamento = tipoAccertamento;
	}

	@Override
	public String toString() {
		return "Accertamento [id=" + accId + ", codiceAcc=" + codiceAcc
				+ ", nome=" + nome + ", descrizione=" + descrizione
				+ ", tipoAccertamento=" + tipoAccertamento + "]";
	}

}
