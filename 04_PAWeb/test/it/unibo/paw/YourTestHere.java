package it.unibo.paw;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import it.unibo.paw.db.DataSource;
import it.unibo.paw.db.PersistenceException;
import it.unibo.paw.model.PrenotazioniRepository;
import it.unibo.paw.model.TavoloRepository;

public class YourTestHere {

	@Test
	public static void main(String args[]) {
		PrenotazioniRepository sr = new PrenotazioniRepository(DataSource.DB2);
		TavoloRepository tr = new TavoloRepository(DataSource.DB2);
		try {
			tr.dropAndCreateTables();
			sr.dropAndCreateTables();
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
