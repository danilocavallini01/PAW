package it.unibo.paw.dao;

import java.util.Map;

public interface StadioDAO {

	// -------------------------------------

	public void create(StadioDTO stadio);

	public StadioDTO read(int id);

	public boolean update(StadioDTO stadio);

	public boolean delete(int id);

	// -------------------------------------

	public Map<String, Integer> totalGamesGroupedByCategory(int id);

	// -------------------------------------

	public boolean createTable();

	public boolean dropTable();
}
