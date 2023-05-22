package it.unibo.paw.dao.mysql;

import java.util.List;

import it.unibo.paw.dao.PiattoDTO;
import it.unibo.paw.dao.RistoranteDTO;
import it.unibo.paw.dao.RistorantePiattoMappingDAO;

public class MySqlRistoranteDTOProxy extends RistoranteDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public MySqlRistoranteDTOProxy() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public List<PiattoDTO> getPiatti()
	{
		if(isAlreadyLoaded())
			return super.getPiatti();
		else
		{
			RistorantePiattoMappingDAO rpm = new MySqlRistorantePiattoMappingDAO();
			isAlreadyLoaded(true);
			return rpm.getPiattiFromResturant(this.getId());
		}
	}

}
