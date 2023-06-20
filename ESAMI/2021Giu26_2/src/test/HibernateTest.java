package test;

import java.io.PrintWriter;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.SysexMessage;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import it.unibo.paw.hibernate.Partner;
import it.unibo.paw.hibernate.Progetto;
import it.unibo.paw.hibernate.WorkPackage;
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

	/*
	 * private static final String FIRST_QUERY = "select p.nome,p.cognome from " +
	 * Passeggero.class.getSimpleName() +
	 * " p join p.voliAeroportoBo v where v.localitaDestinazione = 'Amsterdam'";
	 */

	private static String FIRST_QUERY = "select p.nomeProgetto, w.id, prt.nome from " + Progetto.class.getSimpleName()
			+ " p join p.workPackages w left join w.partners prt " + " where p.annoInizio = 2018";

	public static void main(String[] args) {// throws Exception{

		Session session = null;
		Transaction tx = null;

		try {
			HibernateUtil.dropAndCreateTables();

			// Persistenza
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Progetto p = new Progetto();

			p.setId(1);
			p.setCodiceProgetto(1);
			p.setNomeProgetto("progetto");
			p.setAnnoInizio(2018);
			p.setDurata("2 anni");

			session.persist(p);

			WorkPackage wp1 = new WorkPackage();
			wp1.setId(1);
			wp1.setNome("Wp1");
			wp1.setTitolo("Package di aiuto");
			wp1.setDescrizione("no description");

			WorkPackage wp2 = new WorkPackage();
			wp2.setId(2);
			wp2.setNome("Wp2");
			wp2.setTitolo("Package di persistenza");
			wp2.setDescrizione("no description");

			wp1.setProgetto(p);
			wp2.setProgetto(p);

			session.persist(wp1);
			session.persist(wp2);

			Partner p1 = new Partner();
			p1.setId(1);
			p1.setSiglaPartner("SGC");
			p1.setNome("DANILO");

			p1.getWorkPackages().add(wp1);
			p1.getWorkPackages().add(wp2);
			
			wp1.getPartners().add(p1);
			wp2.getPartners().add(p1);

			Partner p2 = new Partner();
			p2.setId(2);
			p2.setSiglaPartner("dsd");
			p2.setNome("Marco");

			p2.getWorkPackages().add(wp2);
			
			wp2.getPartners().add(p2);

			session.persist(p1);
			session.persist(p2);

			tx.commit();
			session.close();

			session = HibernateUtil.getSessionFactory().openSession();

			Query firstQuery = session.createQuery(FIRST_QUERY);

			PrintWriter pw = new PrintWriter("Progetti-2018.txt");

			pw.println("PRIMA QUERY");
			List<Object[]> res = firstQuery.list();

			pw.close();

		} catch (Exception e1) {
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			e1.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}
}