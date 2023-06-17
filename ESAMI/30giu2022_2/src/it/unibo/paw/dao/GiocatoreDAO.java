package it.unibo.paw.dao;

public interface GiocatoreDAO {

	//-------------------------------------

	public void create (GiocatoreDTO giocatore);

	public GiocatoreDTO read (int id);

	public boolean update (GiocatoreDTO giocatore);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
