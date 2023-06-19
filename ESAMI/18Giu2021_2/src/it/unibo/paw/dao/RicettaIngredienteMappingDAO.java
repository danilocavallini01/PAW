package it.unibo.paw.dao;

import java.util.List;

public interface RicettaIngredienteMappingDAO {

	// --- CRUD -------------
	public void create(int idRicetta, int idIngrediente);

	public boolean delete(int idRicetta, int idIngrediente);

	// ----------------------------------
	public List<RicettaDTO> getRicetteFromIngrediente(int id);
	
	public List<String> getRicetteContenentiAglio();

	// ----------------------------------
	public boolean createTable();

	public boolean dropTable();
}
