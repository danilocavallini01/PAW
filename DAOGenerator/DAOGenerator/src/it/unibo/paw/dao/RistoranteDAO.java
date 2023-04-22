package it.unibo.paw.dao;

public interface RistoranteDAO {

	//-------------------------------------

	public void create (RistoranteDTO Ristorante);

	public RistoranteDTO read (int code);

	public boolean update (RistoranteDTO Ristorante);

	public boolean update (int code);

	//-------------------------------------


	public boolean createTable();

	public boolean dropTable();
}
