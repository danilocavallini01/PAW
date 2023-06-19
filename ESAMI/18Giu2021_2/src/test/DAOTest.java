package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import it.unibo.paw.dao.DAOFactory;
import it.unibo.paw.dao.IngredienteDAO;
import it.unibo.paw.dao.IngredienteDTO;
import it.unibo.paw.dao.RicettaDAO;
import it.unibo.paw.dao.RicettaDTO;
import it.unibo.paw.dao.RicettaIngredienteMappingDAO;

public class DAOTest {
	private static RicettaIngredienteMappingDAO mappingDAO;

	private static List<String> getRicetteAglio() {
		return mappingDAO.getRicetteContenentiAglio();
	}

	public static void main(String[] args) {
		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAOFactory.DB2);

		IngredienteDAO ingredienteDAO = daoFactoryInstance.getIngredienteDAO();
		ingredienteDAO.dropTable();
		ingredienteDAO.createTable();

		RicettaDAO ricettaDAO = daoFactoryInstance.getRicettaDAO();
		ricettaDAO.dropTable();
		ricettaDAO.createTable();

		mappingDAO = daoFactoryInstance.getRicettaIngredienteMappingDAO();
		mappingDAO.dropTable();
		mappingDAO.createTable();

		IngredienteDTO i1 = new IngredienteDTO();
		i1.setId(1);
		i1.setNome("aglio");
		i1.setQuantita(2);

		IngredienteDTO i2 = new IngredienteDTO();
		i2.setId(2);
		i2.setNome("peperoncino");
		i2.setQuantita(1);

		ingredienteDAO.create(i1);
		ingredienteDAO.create(i2);

		RicettaDTO r1 = new RicettaDTO();
		r1.setId(1);
		r1.setNome("aglio olio e peperoncino");
		r1.setTempoPreparazione(2);
		r1.setLivelloDifficolta("Facile");
		r1.setCalorie(200);

		RicettaDTO r2 = new RicettaDTO();
		r2.setId(2);
		r2.setNome("nduja");
		r2.setTempoPreparazione(40);
		r2.setLivelloDifficolta("Molto alto");
		r2.setCalorie(800);

		ricettaDAO.create(r1);
		ricettaDAO.create(r2);

		mappingDAO.create(r1.getId(), i1.getId());
		mappingDAO.create(r1.getId(), i2.getId());

		mappingDAO.create(r2.getId(), i2.getId());
		
		IngredienteDTO testLazyLoad = ingredienteDAO.read(i2.getId());
		
		// PRIMA DEL LAZY LOAD
		System.out.println(testLazyLoad);
		
		// DOPO IL LAZY LOAD
		testLazyLoad.getRicette();
		System.out.println(testLazyLoad);
		
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("Aglio.txt");

			List<String> result = mappingDAO.getRicetteContenentiAglio();
			pw.println("Ricette contenti aglio");

			for (String s : result) {
				System.out.println(s);
				pw.println(s);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if ( pw != null ) {
				pw.close();
			}
		}
	}
}
