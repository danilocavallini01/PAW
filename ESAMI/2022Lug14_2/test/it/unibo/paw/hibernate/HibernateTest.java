package it.unibo.paw.hibernate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
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

	/*
	 * private static final String FIRST_QUERY =
	 * "SELECT a.NOME, count(DISTINCT o.FORMATO)  FROM ARCHIVIO_FISICO a " +
	 * "JOIN MATERIALE_FISICO m ON m.archivio_id = a.id " +
	 * "JOIN OGGETTO_DIGITALE o ON o.materiale_id = m.id " +
	 * "WHERE YEAR(a.dataCreazione) > 2014 GROUP BY a.NOME ";
	 */

	/*
	 * private static final String FIRST_QUERY =
	 * "select count(distinct o.formato) from " +
	 * OggettoDigitale.class.getSimpleName() +
	 * " o join o.materiale_id m join m.archivio_id a where year(a.dataCreazione) > 2014"
	 * ;
	 */

	private static final String FIRST_QUERY = "select a.nome, count(distinct o.formato) from "
			+ ArchivioFisico.class.getSimpleName()
			+ " a join a.materiali m join m.oggetti o where year(a.dataCreazione) > 2014 group by a.nome";
	
	private static final String SECOND_QUERY = "select distinct a.nome from " + ArchivioFisico.class.getSimpleName()
			+ " a join a.materiali m join m.oggetti o where o.formato = 'jpeg'";

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
			calendar.set(2022, 1, 1);
			a.setDataCreazione(calendar.getTime());

			ArchivioFisico a2 = new ArchivioFisico();
			a2.setId(2);
			a2.setCodiceArchivio(2);
			a2.setNome("archvio2");
			a2.setDescrizione("archvio2");
			calendar.set(2022, 1, 1);
			a2.setDataCreazione(calendar.getTime());

			MaterialeFisico m = new MaterialeFisico();
			m.setId(1);
			m.setCodiceMateriale(1);
			m.setNome("materiale1");
			m.setDescrizione("materiale1");
			calendar.set(2022, 1, 1);
			m.setDataCreazione(calendar.getTime());

			MaterialeFisico m2 = new MaterialeFisico();
			m2.setId(2);
			m2.setCodiceMateriale(2);
			m2.setNome("materiale1");
			m2.setDescrizione("materiale1");
			calendar.set(2022, 1, 1);
			m2.setDataCreazione(calendar.getTime());

			OggettoDigitale o = new OggettoDigitale();
			o.setId(1);
			o.setCodiceOggetto(1);
			o.setNome("materiale1");
			o.setFormato("jpeg");
			calendar.set(2022, 1, 1);
			o.setDataDigitalizzazione(calendar.getTime());

			OggettoDigitale o2 = new OggettoDigitale();
			o2.setId(2);
			o2.setCodiceOggetto(2);
			o2.setNome("materiale2");
			o2.setFormato("jpeg");
			calendar.set(2020, 3, 1);
			o2.setDataDigitalizzazione(calendar.getTime());

			session.persist(o);
			session.persist(o2);

			Set<OggettoDigitale> oggetti = new HashSet<OggettoDigitale>();
			oggetti.add(o2);
			oggetti.add(o);
			Set<OggettoDigitale> oggetti2 = new HashSet<OggettoDigitale>();
			oggetti2.add(o);
			m.setOggetti(oggetti);
			m2.setOggetti(oggetti2);

			Set<MaterialeFisico> materiali = new HashSet<MaterialeFisico>();
			materiali.add(m);

			Set<MaterialeFisico> materiali2 = new HashSet<MaterialeFisico>();
			materiali2.add(m2);

			a.setMateriali(materiali);
			a2.setMateriali(materiali2);

			session.persist(m);
			session.persist(m2);
			session.persist(a);
			session.persist(a2);

			tx.commit();
			session.close();

			session = HibernateUtil.getSessionFactory().openSession();

			Query firstQuery = session.createQuery(FIRST_QUERY);

			PrintWriter pw = new PrintWriter(new FileWriter("Archivio.txt"));
			
			pw.println("PRIMA QUERY");
			List<Object[]> res = firstQuery.list();
			for (Object[] result : res) {
				System.out.println(result[0] + "-" + result[1]);
				pw.println(result[0] + "-" + result[1]);
			}
			
			Query secondQuery = session.createQuery(SECOND_QUERY);

			pw.println("\nSECONDA QUERY");
			List<String> res2 = secondQuery.list();
			for (String result : res2) {
				System.out.println(result);
				pw.println(result);
			}
			
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}

			if (session.isOpen()) {
				session.close();
			}
		}
	}
}
