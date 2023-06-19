package it.unibo.paw.dao;

public interface RicettaDAO {

	//-------------------------------------

	public void create (RicettaDTO ricetta);

	public RicettaDTO read (int id);

	public boolean update (RicettaDTO ricetta);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
