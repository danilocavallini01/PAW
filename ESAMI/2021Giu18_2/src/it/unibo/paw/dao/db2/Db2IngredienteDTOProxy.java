package it.unibo.paw.dao.db2;

import java.util.List;

import it.unibo.paw.dao.IngredienteDTO;
import it.unibo.paw.dao.RicettaDTO;
import it.unibo.paw.dao.RicettaIngredienteMappingDAO;

public class Db2IngredienteDTOProxy extends IngredienteDTO {
	
	private static final long serialVersionUID = 1L;

	public Db2IngredienteDTOProxy() {
		super();
	}
	
	public List<RicettaDTO> getRicette() {
		
		if ( this.isLoaded() ) {
			return super.getRicette();
		} else {
			RicettaIngredienteMappingDAO mapping = new Db2RicettaIngredienteMappingDAO();
			super.setRicette(mapping.getRicetteFromIngrediente(this.getId()));
			this.setLoaded(true);
			return super.getRicette();
		}
	}
}
