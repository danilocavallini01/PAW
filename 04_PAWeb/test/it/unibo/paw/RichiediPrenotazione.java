package it.unibo.paw;

import java.util.Date;

import it.unibo.paw.db.DataSource;
import it.unibo.paw.db.PersistenceException;
import it.unibo.paw.model.PrenotazioniRepository;
import it.unibo.paw.model.TavoloRepository;

public class RichiediPrenotazione {

	public static void main(String[] args) {
		PrenotazioniRepository sr = new PrenotazioniRepository(DataSource.DB2);
		TavoloRepository tr = new TavoloRepository(DataSource.DB2);
		try {
			
			sr.richiestaPrenotazione("danilo", new Date(), 2,"3428851345", tr);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
