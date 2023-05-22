package it.unibo.paw.dao;

import java.util.List;

public interface RistoranteDAO {
	
	// --- CRUD -------------
		public void create(RistoranteDTO ristorante);

		public RistoranteDTO read(int id);

		public boolean update(RistoranteDTO ristorante);

		public boolean delete(int id);
		// ----------------------------------
		public List<RistoranteDTO> getResturantByCity(String citta);
		
		public List<RistoranteDTO> getRatedResturant(int stars);
		// ----------------------------------	
		public boolean createTable();

		public boolean dropTable();
	
}
