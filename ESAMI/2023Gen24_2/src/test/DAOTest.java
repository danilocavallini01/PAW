package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import it.unibo.paw.dao.DAOFactory;
import it.unibo.paw.dao.PartitaDAO;
import it.unibo.paw.dao.PartitaDTO;
import it.unibo.paw.dao.StadioDAO;
import it.unibo.paw.dao.StadioDTO;

public class DAOTest {
public static final int DAO = DAOFactory.DB2;
	
	public static void main(String[] args) {
		
		// Student
		
		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAO);
		
		StadioDAO stadioDAO = daoFactoryInstance.getStadioDAO();
		stadioDAO.dropTable();
		stadioDAO.createTable();
		
		PartitaDAO partitaDAO = daoFactoryInstance.getPartitaDAO();
		partitaDAO.dropTable();
		partitaDAO.createTable();
		
		StadioDTO s = new StadioDTO();
		s.setId(1);
		s.setCodice(1);
		s.setNome("dallara");
		s.setCitta("Bologna");
		
		stadioDAO.create(s);
		
		Calendar c = Calendar.getInstance();
		c.set(1984, 1, 24);
		
		
		PartitaDTO p = new PartitaDTO();
		p.setId(1);
		p.setCodicePartita(1);
		p.setCategoria("Serie B");
		p.setGirone("A");
		p.setNomeSquadraCasa("NAPOLI");
		p.setNomeSquadraOspite("BOLOGNA");
		p.setData(c.getTime());
		
		partitaDAO.create(p, s);
		
		
		PartitaDTO p2 = new PartitaDTO();
		p2.setId(2);
		p2.setCodicePartita(2);
		p2.setCategoria("Serie A");
		p2.setGirone("B");
		p2.setNomeSquadraCasa("NAPOLI");
		p2.setNomeSquadraOspite("NAPOLI");
		p2.setData(c.getTime());
		
		partitaDAO.create(p2, s);
		
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter("Partite.txt");
			pw.println("CATEGORIA - NUMPARTITE");
			for( Map.Entry<String, Integer> entry : stadioDAO.totalGamesGroupedByCategory(s.getId()).entrySet() ) {
				pw.println(entry.getKey() + " - " + entry.getValue());
			}
			
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if ( pw != null ) {
				pw.close();
			}
		}
	
		
	}
}
