package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import it.unibo.paw.db.*;

import it.unibo.paw.model.*;

public class Test {

	public static void main(String[] args) {

		AcquistoRepository ar = new AcquistoRepository(DataSource.DB2);

		try {
			ar.dropTable();
			ar.createTable();
			
			Acquisto a1 = new Acquisto();
			a1.setCodiceAcquisto(1);
			a1.setImporto(900);
			a1.setNomeAcquirente("Danilo");
			a1.setCognomeAcquirente("Cavallini");

			Acquisto a2 = new Acquisto();
			a2.setCodiceAcquisto(2);
			a2.setImporto(1500);
			a2.setNomeAcquirente("Danilo1");
			a2.setCognomeAcquirente("Cavallini1");

			Acquisto a3 = new Acquisto();
			a3.setCodiceAcquisto(3);
			a3.setImporto(200);
			a3.setNomeAcquirente("Danilo2");
			a3.setCognomeAcquirente("Cavallini2");

			ar.persist(a1);
			ar.persist(a2);
			ar.persist(a3);

			PrintWriter pw = new PrintWriter("Acquisto.txt");

			List<Integer> result = ar.codiciAcquistoConImportoMaggioreDi(1000);
			System.out.println(result);
			pw.println(result);
			
			a3.setImporto(1300);
			ar.update(a3);

			result = ar.codiciAcquistoConImportoMaggioreDi(1000);
			System.out.println(result);
			pw.println(result);

			ar.delete(a1.getId());
			ar.delete(a2.getId());
			ar.delete(a3.getId());
			
			result = ar.codiciAcquistoConImportoMaggioreDi(1000);
			System.out.println(result);
			pw.println(result);
			
			pw.close();

		} catch (PersistenceException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
