package it.unibo.paw.dao;

public interface IngredienteDAO {

	//-------------------------------------

	public void create (IngredienteDTO ingrediente);

	public IngredienteDTO read (int id);

	public boolean update (IngredienteDTO ingrediente);

	public boolean delete (int id);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
