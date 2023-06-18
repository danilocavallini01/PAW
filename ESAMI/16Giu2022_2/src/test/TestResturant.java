package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import it.unibo.paw.db.*;

import it.unibo.paw.model.*;

public class TestResturant {

	public static void main(String[] args) {
		SupermercatoRepository sr = new SupermercatoRepository(DataSource.DB2);
		ProdottoRepository pr = new ProdottoRepository(DataSource.DB2);

		try {
			sr.dropTable();
			pr.dropTable();
			pr.createTable();
			sr.createTable();

			ProdottoOfferto p1 = new ProdottoOfferto();
			p1.setCodiceProdotto(1);
			p1.setDescrizione("Vaniglia");
			p1.setMarca("Nestle");
			p1.setPrezzo(10);

			ProdottoOfferto p2 = new ProdottoOfferto();
			p2.setCodiceProdotto(2);
			p2.setDescrizione("Pollo");
			p2.setMarca("KFC");
			p2.setPrezzo(12);

			Supermercato s = new Supermercato();
			s.setCodiceSuper(1);
			s.setNome("Coop");
			s.setRatingGradimento(2);

			Supermercato s2 = new Supermercato();
			s2.setCodiceSuper(2);
			s2.setNome("Lidl");
			s2.setRatingGradimento(4);

			sr.persist(s);
			sr.persist(s2);

			pr.persist(p2, s2.getCodiceSuper());
			pr.persist(p1, s.getCodiceSuper());
			
			PrintWriter pw = new PrintWriter("Supermercato.txt");
			
			pw.println(sr.prodottoOfferto("Lidl", "Pollo", "KFC"));
			pw.println(sr.prodottoOfferto("Coop", "Pollo", "KFC"));
			
			System.out.println(sr.prodottoOfferto("Lidl", "Pollo", "KFC"));
			System.out.println(sr.prodottoOfferto("Coop", "Pollo", "KFC"));
			
			pw.close();
		} catch (PersistenceException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
