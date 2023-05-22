package it.unibo.paw.dao.db2;

import java.util.List;

import it.unibo.paw.dao.PiattoDTO;
import it.unibo.paw.dao.RistoranteDTO;
import it.unibo.paw.dao.RistorantePiattoMappingDAO;

public class Db2RistoranteDTOProxy extends RistoranteDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Db2RistoranteDTOProxy() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<PiattoDTO> getPiatti() {
		if (isAlreadyLoaded())
			return super.getPiatti();
		else {
			RistorantePiattoMappingDAO rpm = new Db2RistorantePiattoMappingDAO();
			setPiatti(rpm.getPiattiFromResturant(this.getId()));
			isAlreadyLoaded(true);
			return getPiatti();
		}
	}

}
