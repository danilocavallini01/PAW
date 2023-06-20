package test;

import java.io.PrintWriter;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import it.unibo.paw.hibernate.Passeggero;
import it.unibo.paw.hibernate.VoloAeroportoBo;
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

	private static final String FIRST_QUERY = "select p.nome,p.cognome from " + Passeggero.class.getSimpleName()
			+ " p join p.voliAeroportoBo v where v.localitaDestinazione = 'Amsterdam'";

	public static void main(String[] args) {// throws Exception{

		Session session = null;
		Transaction tx = null;

		try {
			HibernateUtil.dropAndCreateTables();
			
			//Persistenza
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			Passeggero p1 = new Passeggero();
			p1.setId(1);
			p1.setCodPasseggero(1);
			p1.setNome("Danilo");
			p1.setCognome("Cavallini");
			p1.setNumPassaporto("A9d");
			
			session.persist(p1);
			
			Passeggero p2 = new Passeggero();
			p2.setId(2);
			p2.setCodPasseggero(2);
			p2.setNome("Alessandro");
			p2.setCognome("Cavallini");
			p2.setNumPassaporto("Add9d");
			
			session.persist(p2);
			
			VoloAeroportoBo v1 = new VoloAeroportoBo();
			v1.setId(1);
			v1.setCodVolo(1);
			v1.setCompagniaAerea("Luftansa");
			v1.setLocalitaDestinazione("Amsterdam");
			v1.setDataPartenza(new Date());
			v1.setOrarioPartenza(Time.valueOf("02:22:01"));
			
			v1.getPasseggeri().add(p1);
			v1.getPasseggeri().add(p2);
			
			session.persist(v1);
			
			VoloAeroportoBo v2 = new VoloAeroportoBo();
			v2.setId(2);
			v2.setCodVolo(2);
			v2.setCompagniaAerea("Ryanayr");
			v2.setLocalitaDestinazione("Castel Maggiore");
			v2.setDataPartenza(new Date());
			v2.setOrarioPartenza(Time.valueOf("07:22:01"));
			
			v2.getPasseggeri().add(p2);
			session.persist(v2);
			
			tx.commit();
			session.close();

			session = HibernateUtil.getSessionFactory().openSession();
			
			Query firstQuery = session.createQuery(FIRST_QUERY);

			PrintWriter pw = new PrintWriter("AeroportoBO.txt");
			
			pw.println("PRIMA QUERY");
			List<Object[]> res = firstQuery.list();
			
			for (Object[] result : res) {
				System.out.println(result[0] + " " + result[1]);
				pw.println(result[0] + " " + result[1]);
			}
			
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