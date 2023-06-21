package it.unibo.paw.dao;

public interface SquadraPallacanestroDAO {

	//-------------------------------------

	public void create (SquadraPallacanestroDTO squadrapallacanestro);

	public SquadraPallacanestroDTO read (int id);

	public boolean update (SquadraPallacanestroDTO squadrapallacanestro);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
