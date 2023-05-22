package it.unibo.paw.dao;

public interface PartitaDAO {

	//-------------------------------------

	public void create (PartitaDTO partita);

	public PartitaDTO read (int id);

	public boolean update (PartitaDTO partita);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
