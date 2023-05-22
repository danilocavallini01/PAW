package it.unibo.paw.dao;

import java.util.List;
import java.io.*;

public class DAOTest {

	static PrintWriter pw = null;

	public static final int DAO = DAOFactory.DB2;

	public static String ListPrimiPiattiDeiRistorantiDiBologna(RistoranteDAO r, RistorantePiattoMappingDAO rpm) {
		List<RistoranteDTO> ristorantiBolognesi = r.getResturantByCity("Bologna");
		String result = "";
		for (RistoranteDTO risto : ristorantiBolognesi) {
			boolean trovato = false;
			List<PiattoDTO> primiPiatti = risto.getPiatti();
			for (PiattoDTO p : primiPiatti) {
				if (p.getTipo().compareTo("primo") == 0) {
					result = result + p + "\n";
					trovato = true;
				}
			}
			if (trovato) {
				result = result + "Questo/i piatto/i è/sono preparato/i da: " + risto.getNomeRistorante() + "\n";
			}
		}
		return result;
	}

	public static String CountRatedResturantsWithSeppieEPiselli(RistoranteDAO r,
			RistorantePiattoMappingDAO rpm) {
		List<RistoranteDTO> ristorantiStellati = r.getRatedResturant(4);
		int counter = 0;
		for (RistoranteDTO risto : ristorantiStellati) {
			List<PiattoDTO> primiPiatti = risto.getPiatti();
			for (PiattoDTO p : primiPiatti) {
				if (p.getNomePiatto().compareTo("Seppie e Piselli") == 0 &&
						p.getTipo().compareToIgnoreCase("secondo") == 0) {
					counter++;
					break;
				}
			}
		}
		return "Sono stati trovati " + counter + " ristoranti con almeno 4 stelle che preparano"
				+ " Seppie e Piselli come Secondo piatto";
	}

	public static void main(String[] args) {
		DAOFactory daoFactoryInstance = DAOFactory.getDAOFactory(DAO);
		
		RistorantePiattoMappingDAO mappingDAO = daoFactoryInstance.getRistorantePiattoMappingDAO();
		mappingDAO.dropTable();
		
		// Ristoranti
		
		RistoranteDAO ristoranteDAO = daoFactoryInstance.getRistoranteDAO();
		ristoranteDAO.dropTable();
		ristoranteDAO.createTable();

		RistoranteDTO r = new RistoranteDTO();
		r.setId(1);
		r.setNomeRistorante("E' cucina");
		r.setIndirizzo("Via G. Leopardi, 24, Bologna");
		r.setRating(5);
		ristoranteDAO.create(r);

		r = new RistoranteDTO();
		r.setId(2);
		r.setNomeRistorante("Camst");
		r.setIndirizzo("Viale Risorgimento, 2, Bologna");
		r.setRating(2);
		ristoranteDAO.create(r);

		r = new RistoranteDTO();
		r.setId(3);
		r.setNomeRistorante("Ca' Pelletti");
		r.setIndirizzo("Viale del Brodo, 60, Modena");
		r.setRating(4);
		ristoranteDAO.create(r);

		for (RistoranteDTO br : ristoranteDAO.getResturantByCity("Bologna")) {
			System.out.println(br);
		}
		System.out.println();

		// Piatti

		PiattoDAO piattoDAO = daoFactoryInstance.getPiattoDAO();
		piattoDAO.dropTable();
		piattoDAO.createTable();

		PiattoDTO p = new PiattoDTO();
		p.setId(1);
		p.setNomePiatto("Lasagne commestibili");
		p.setTipo("primo");
		piattoDAO.create(p);

		p = new PiattoDTO();
		p.setId(2);
		p.setNomePiatto("Passatelli Gourmet");
		p.setTipo("primo");
		piattoDAO.create(p);

		p = new PiattoDTO();
		p.setId(3);
		p.setNomePiatto("Seppie e Piselli");
		p.setTipo("secondo");
		piattoDAO.create(p);

		p = new PiattoDTO();
		p.setId(4);
		p.setNomePiatto("Tortellini in Brodo");
		p.setTipo("primo");
		piattoDAO.create(p);

		p = piattoDAO.findByName("Seppie e Piselli");
		System.out.println(p);
		System.out.println();

		// StudentCoursesMapping

		mappingDAO.createTable();

		mappingDAO.create(2, 1);

		mappingDAO.create(1, 2);

		mappingDAO.create(3, 1);

		mappingDAO.create(2, 3);

		mappingDAO.create(3, 3);

		mappingDAO.create(3, 4);

		mappingDAO.create(1, 4);

		try {
			pw = new PrintWriter(new FileWriter("ristorante.txt"));

			// Primi piatti dei ristoranti di Bologna
			String outPut = ListPrimiPiattiDeiRistorantiDiBologna(ristoranteDAO, mappingDAO);
			pw.println(outPut);
			System.out.println(outPut);
			System.out.println();
			// Numero di ristoranti con almeno 4 stelle che preparano Seppie e Piselli
			outPut = CountRatedResturantsWithSeppieEPiselli(ristoranteDAO, mappingDAO);
			pw.append("\n" + outPut);
			System.out.println(outPut);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println();
		System.out.println();
	}

}
