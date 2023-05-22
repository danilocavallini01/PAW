package it.unibo.paw.dao;

public interface PiattoDAO {
	
	// --- CRUD -------------
	public void create(PiattoDTO piatto);

	public PiattoDTO read(int id);

	public boolean update(PiattoDTO piatto);

	public boolean delete(int id);	
	// ----------------------------------
	
	public PiattoDTO findByName(String nome);
	
	// ----------------------------------
	
	public boolean createTable();

	public boolean dropTable();
}
