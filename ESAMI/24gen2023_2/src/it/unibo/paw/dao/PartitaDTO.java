package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class PartitaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// -------------------------------------

	private int id;
	private boolean loaded;
	private int CodicePartita;
	private String Categoria;
	private String Girone;
	private String NomeSquadraCasa;
	private String NomeSquadraOspite;
	private Date Data;

	// -------------------------------------

	public PartitaDTO() {
		this.loaded = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public int getCodicePartita() {
		return CodicePartita;
	}

	public void setCodicePartita(int codicePartita) {
		CodicePartita = codicePartita;
	}

	public String getCategoria() {
		return Categoria;
	}

	public void setCategoria(String categoria) {
		Categoria = categoria;
	}

	public String getGirone() {
		return Girone;
	}

	public void setGirone(String girone) {
		Girone = girone;
	}

	public String getNomeSquadraCasa() {
		return NomeSquadraCasa;
	}

	public void setNomeSquadraCasa(String nomeSquadraCasa) {
		NomeSquadraCasa = nomeSquadraCasa;
	}

	public String getNomeSquadraOspite() {
		return NomeSquadraOspite;
	}

	public void setNomeSquadraOspite(String nomeSquadraOspite) {
		NomeSquadraOspite = nomeSquadraOspite;
	}

	public Date getData() {
		return Data;
	}

	public void setData(Date data) {
		Data = data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Categoria, CodicePartita, Data, Girone, NomeSquadraCasa, NomeSquadraOspite, id, loaded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartitaDTO other = (PartitaDTO) obj;
		return Objects.equals(Categoria, other.Categoria) && CodicePartita == other.CodicePartita
				&& Objects.equals(Data, other.Data) && Objects.equals(Girone, other.Girone)
				&& Objects.equals(NomeSquadraCasa, other.NomeSquadraCasa)
				&& Objects.equals(NomeSquadraOspite, other.NomeSquadraOspite) && id == other.id
				&& loaded == other.loaded;
	}

}
