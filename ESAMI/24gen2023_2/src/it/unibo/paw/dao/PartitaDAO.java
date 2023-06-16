package it.unibo.paw.dao;

public interface PartitaDAO {

	//-------------------------------------

	public void create (PartitaDTO partita, StadioDTO stadioAssociato);

	public PartitaDTO read (int id);

	public boolean update (PartitaDTO partita, StadioDTO stadioAssociato);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
