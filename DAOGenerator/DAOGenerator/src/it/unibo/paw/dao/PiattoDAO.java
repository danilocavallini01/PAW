package it.unibo.paw.dao;

public interface PiattoDAO {

	//-------------------------------------

	public void create (PiattoDTO Piatto);

	public PiattoDTO read (int code);

	public boolean update (PiattoDTO Piatto);

	public boolean update (int code);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
