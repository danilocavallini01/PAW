package test;

import java.util.Calendar;
import java.util.List;

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
		
		PartitaDAO partitaDAO = daoFactoryInstance.getPartitaDAO();
		partitaDAO.dropTable();
		partitaDAO.createTable();
		
		StadioDAO stadioDAO = daoFactoryInstance.getStadioDAO();
		stadioDAO.dropTable();
		partitaDAO.createTable();
		
		StadioDTO s = new StadioDTO();
		s.setCodice(1);
		s.setNome("dallara");
		s.setCitta("Bologna");
		
		stadioDAO.create(s);
		
		Calendar c = Calendar.getInstance();
		c.set(1984, 1, 24);
		
		
		PartitaDTO p = new PartitaDTO();
		p.setCodicePartita(1);
		p.setCategoria("1");
		p.setGirone("A");
		p.setNomeSquadraCasa("NAPOLI");
		p.setNomeSquadraOspite("BOLOGNA");
		p.setData(c.getTime());
		
		
		PartitaDTO p2 = new PartitaDTO();
		p.setCodicePartita(2);
		p.setCategoria("2");
		p.setGirone("B");
		p.setNomeSquadraCasa("NAPOLI");
		p.setNomeSquadraOspite("NAPOLI");
		p.setData(c.getTime());
		
		//tempo 53.06
		
	}
}
