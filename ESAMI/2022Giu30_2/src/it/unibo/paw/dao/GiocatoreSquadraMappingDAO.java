package it.unibo.paw.dao;

import java.util.List;

public interface GiocatoreSquadraMappingDAO {
	public void create(int idGiocatore, int idSquadra);

	public boolean delete(int idGiocatore, int idSquadra);

	// ----------------------------------

	// ----------------------------------

	public boolean createTable();

	public boolean dropTable();

	public List<GiocatoreDTO> getGiocatoriBySquadra(int id);

	public List<SquadrapallacanestroDTO> getSquadreByGiocatore(int id);
}