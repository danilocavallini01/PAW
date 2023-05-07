package it.unibo.paw.hibernate;

import java.util.Objects;
import java.util.Set;

public class TipoAccertamento {
	private int id;
	private String codiceTipoAccertamento;
	private String descrizione;

	private Set<Ospedale> ospedali;
	private Set<Accertamento> accertamenti;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodiceTipoAccertamento() {
		return codiceTipoAccertamento;
	}

	public void setCodiceTipoAccertamento(String codiceTipoAccertamento) {
		this.codiceTipoAccertamento = codiceTipoAccertamento;
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
	public int hashCode() {
		return Objects.hash(accertamenti, codiceTipoAccertamento, descrizione, id, ospedali);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoAccertamento other = (TipoAccertamento) obj;
		return Objects.equals(accertamenti, other.accertamenti)
				&& Objects.equals(codiceTipoAccertamento, other.codiceTipoAccertamento)
				&& Objects.equals(descrizione, other.descrizione) && id == other.id
				&& Objects.equals(ospedali, other.ospedali);
	}

}
