package it.unibo.paw.dao;

public interface SquadrapallacanestroDAO {

	//-------------------------------------

	public void create (SquadrapallacanestroDTO squadrapallacanestro);

	public SquadrapallacanestroDTO read (int id);

	public boolean update (SquadrapallacanestroDTO squadrapallacanestro);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
