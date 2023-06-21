package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import it.unibo.paw.dao.DAOFactory;
import it.unibo.paw.dao.GiocatoreDAO;
import it.unibo.paw.dao.GiocatoreDTO;
import it.unibo.paw.dao.GiocatoreSquadraMappingDAO;
import it.unibo.paw.dao.SquadraPallacanestroDAO;
import it.unibo.paw.dao.SquadraPallacanestroDTO;
import it.unibo.paw.dao.db2.Db2CustomQuery;

public class DAOTest {
	public static final int DAO = DAOFactory.DB2;

	public static void main(String[] args) {

		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAO);

		GiocatoreDAO giocatoreDAO = daoFactoryInstance.getGiocatoreDAO();
		giocatoreDAO.dropTable();
		giocatoreDAO.createTable();

		SquadraPallacanestroDAO squadraDAO = daoFactoryInstance.getSquadrapallacanestroDAO();
		squadraDAO.dropTable();
		squadraDAO.createTable();

		GiocatoreSquadraMappingDAO mappingDAO = daoFactoryInstance.getGiocatoreSquadraMappingDAO();
		mappingDAO.dropTable();
		mappingDAO.createTable();

		GiocatoreDTO g1 = new GiocatoreDTO();
		g1.setCodiceFiscale("C1323");
		g1.setCognome("Bellavista");
		g1.setNome("paolo");
		g1.setEta(22);

		GiocatoreDTO g2 = new GiocatoreDTO();
		g2.setCodiceFiscale("dsdaxC1323");
		g2.setCognome("Patella");
		g2.setNome("Patella");
		g2.setEta(23);

		giocatoreDAO.create(g1);
		giocatoreDAO.create(g2);

		SquadraPallacanestroDTO s = new SquadraPallacanestroDTO();
		s.setNome("Savena");
		s.setTorneo("Under 30");
		s.setAllenatore("Berlusconi");

		squadraDAO.create(s);

		mappingDAO.create(g1.getId(), s.getId());
		mappingDAO.create(g2.getId(), s.getId());

		PrintWriter pw = null;

		System.out.println(g1.getId() + " - " + g2.getId());
		try {
			pw = new PrintWriter("Partite.txt");

			pw.println("NOME GIOCATORE - ALLENATORE");

			for (Map.Entry<GiocatoreDTO, String> e : Db2CustomQuery.getAllenatoryByGiocatory().entrySet()) {
				pw.println(e.getKey().getNome() + " - " + e.getValue());
				System.out.println(e.getKey().getNome() + "-" + e.getValue());
			}

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

	}
}
