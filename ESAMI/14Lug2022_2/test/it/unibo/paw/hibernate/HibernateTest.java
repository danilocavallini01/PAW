package it.unibo.paw.hibernate;

import java.io.FileWriter;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.unibo.paw.hibernate.util.HibernateUtil;

public class HibernateTest {
	/*
	 * private static final String FIRSTQUERY_STR = "select a.nome " + "from " +
	 * Ospedale.class.getSimpleName() + " o " + "join o.tipiAccertamento  ta " +
	 * "join ta.accertamenti  a " + "where o.citta = :cittaOsp " +
	 * "and o.nome = :nomeOsp " + "and ta.descrizione = :descTipoAcc";
	 * 
	 * private static final String SECONDQUERY_STR =
	 * "select o.nome, o.indirizzo, o.citta, count(a)" + "from " +
	 * Ospedale.class.getSimpleName() + " o " + "join o.tipiAccertamento ta " +
	 * "join ta.accertamenti a " + "group by o.nome, o.indirizzo, o.citta";
	 */

	public static void main(String[] argv) {

		Session session = null;
		Transaction tx = null;

		try {

			HibernateUtil.dropAndCreateTables();

			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Calendar calendar = Calendar.getInstance();

			ArchivioFisico a = new ArchivioFisico();
			a.setId(1);
			a.setCodiceArchivio(1);
			a.setNome("archvio1");
			a.setDescrizione("archvio1");
			calendar.set(1, 1, 2022);
			a.setDataCreazione(calendar.getTime());

			session.persist(a);

			MaterialeFisico m = new MaterialeFisico();
			m.setId(1);
			m.setCodiceMateriale(1);
			m.setNome("materiale1");
			m.setDescrizione("materiale1");
			calendar.set(1, 1, 2022);
			m.setDataCreazione(calendar.getTime());

			session.persist(m);

			OggettoDigitale o = new OggettoDigitale();
			o.setId(1);
			o.setCodiceOggetto(1);
			o.setNome("materiale1");
			o.setFormato("jpeg");
			calendar.set(1, 1, 2022);
			o.setDataDigitalizzazione(calendar.getTime());
			
			OggettoDigitale o2 = new OggettoDigitale();
			o.setId(2);
			o.setCodiceOggetto(2);
			o.setNome("materiale2");
			o.setFormato("png");
			calendar.set(1, 3, 2020);
			o.setDataDigitalizzazione(calendar.getTime());

			Set<OggettoDigitale> oggetti = new HashSet<OggettoDigitale>();
			oggetti.add(o2);
			oggetti.add(o);
			m.setOggetti(oggetti);
			
			Set<MaterialeFisico> materiali = new HashSet<MaterialeFisico>();
			materiali.add(m);
			a.setMateriali(materiali);
			
			session.persist(m);
			session.persist(a);

			tx.commit();
			session.close();

			/*session = HibernateUtil.getSessionFactory().openSession();

			String firstQueryResult = "";
			String secondQueryResult = "";

			// Approccio DB-Oriented
			Query firstQuery = session.createQuery(FIRSTQUERY_STR);
			firstQuery.setString("cittaOsp", "Bologna");
			firstQuery.setString("nomeOsp", "S.Orsola");
			firstQuery.setString("descTipoAcc", "analisi di laboratorio");
			List<String> firstQueryRecords = firstQuery.list();
			for (String nome : firstQueryRecords) {
				firstQueryResult += nome + "\n";
			}

			Query secondQuery = session.createQuery(SECONDQUERY_STR);
			List<Object[]> secondQueryRecords = secondQuery.list();
			for (Object[] arr : secondQueryRecords) {
				secondQueryResult += arr[0] + " " + arr[1] + " " + arr[2] + "\n Numero Accertamenti erogabili: "
						+ arr[3] + "\n";
			}*/

			/*
			 * //Approccio Java-Oriented Query firstQuery = session.createQuery("from " +
			 * Ospedale.class.getSimpleName() + " where citta = ? and nome = ?");
			 * firstQuery.setString(0, "Bologna"); firstQuery.setString(1, "S.Orsola");
			 * List<Ospedale> ospedaliRes = firstQuery.list();
			 * 
			 * for (Ospedale os : ospedaliRes) { Set<TipoAccertamento> tipiAcc =
			 * os.getTipiAccertamento(); for (TipoAccertamento tipo : tipiAcc) { if
			 * (tipo.getDescrizione().compareTo("analisi di laboratorio") == 0) {
			 * Set<Accertamento> accertamenti = tipo.getAccertamenti(); for (Accertamento
			 * acc : accertamenti) { firstQueryResult = firstQueryResult + acc.getNome() +
			 * "\n"; } } } }
			 * 
			 * Query secondQuery = session.createQuery("from " +
			 * Ospedale.class.getSimpleName()); ospedaliRes = secondQuery.list(); for
			 * (Ospedale os : ospedaliRes) { secondQueryResult = secondQueryResult +
			 * os.getNome() + " " + os.getIndirizzo() + " " + os.getCitta() +
			 * "\n Numero Accertamenti erogabili: "; Set<TipoAccertamento> tipiAcc =
			 * os.getTipiAccertamento(); int numAccCounter = 0; for (TipoAccertamento tipo :
			 * tipiAcc) { numAccCounter += tipo.getAccertamenti().size(); }
			 * secondQueryResult = secondQueryResult + numAccCounter + "\n"; }
			 */

			/*PrintWriter pw = new PrintWriter(new FileWriter("Ospedali.txt"));
			pw.println(firstQueryResult);
			pw.append(secondQueryResult);
			pw.close();
		} catch (Exception e1) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			e1.printStackTrace();*/
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
