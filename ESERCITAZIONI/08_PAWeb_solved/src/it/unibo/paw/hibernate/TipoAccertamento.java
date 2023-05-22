package it.unibo.paw.hibernate;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class TipoAccertamento implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int tipoAccId;
	private int codiceTipoAcc;
	private String  descrizione;
	
	private Set<Ospedale> ospedali = new HashSet<Ospedale>(0);
	private Set<Accertamento> accertamenti = new HashSet<Accertamento>(0);
	
	public TipoAccertamento() {}
	
	public int getTipoAccId() {
		return tipoAccId;
	}
	public void setTipoAccId(int tipoAccId) {
		this.tipoAccId = tipoAccId;
	}
	public int getCodiceTipoAcc() {
		return codiceTipoAcc;
	}
	public void setCodiceTipoAcc(int codiceTipoAcc) {
		this.codiceTipoAcc = codiceTipoAcc;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Set<Ospedale> getOspedali() {
		return ospedali;
	}
	public void setOspedali(Set<Ospedale> ospedali) {
		this.ospedali = ospedali;
	}
	public Set<Accertamento> getAccertamenti() {
		return accertamenti;
	}
	public void setAccertamenti(Set<Accertamento> accertamenti) {
		this.accertamenti = accertamenti;
	}
	@Override
	public String toString() {
		return "TipoAccertamento [id=" + tipoAccId + ", codiceAcc=" + codiceTipoAcc
				+ ", descrizione=" + descrizione + ", ospedali=" + ospedali
				+ ", accertamenti=" + accertamenti + "]";
	}
}
