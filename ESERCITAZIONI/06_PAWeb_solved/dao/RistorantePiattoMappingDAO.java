package it.unibo.paw.dao;

import java.util.List;

public interface RistorantePiattoMappingDAO {
	
		// --- CRUD -------------
		public void create(int idr, int idp);

		public boolean delete(int idRistorante, int idPiatto);		
		// ----------------------------------
		public List<PiattoDTO> getPiattiFromResturant(int id);
		// ----------------------------------		
		public boolean createTable();

		public boolean dropTable();
}
