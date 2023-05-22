package it.unibo.paw.hibernate;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import it.unibo.paw.hibernate.util.HibernateUtil;

public class HibernateTest {
	private static final String FIRSTQUERY_STR = "select a.nome "
			+ "from " + Ospedale.class.getSimpleName() + " o "
			+ "join o.tipiAccertamento  ta "
			+ "join ta.accertamenti  a "
			+ "where o.citta = :cittaOsp "
			+ "and o.nome = :nomeOsp "
			+ "and ta.descrizione = :descTipoAcc";

	private static final String SECONDQUERY_STR = "select o.nome, o.indirizzo, o.citta, count(a)"
			+ "from " + Ospedale.class.getSimpleName() + " o "
			+ "join o.tipiAccertamento ta "
			+ "join ta.accertamenti a "
			+ "group by o.nome, o.indirizzo, o.citta";

	public static void main(String[] argv) {

		Session session = null;
		Transaction tx = null;

		try {
			
			HibernateUtil.dropAndCreateTables();
			
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			TipoAccertamento ta = new TipoAccertamento();
			ta.setCodiceTipoAcc(1);
			ta.setTipoAccId(1);
			ta.setDescrizione("analisi di laboratorio");
			ta.setAccertamenti(new HashSet<Accertamento>());
			session.persist(ta);

			Accertamento accertamento = new Accertamento();
			accertamento.setAccId(1);
			accertamento.setCodiceAcc(1);
			accertamento.setDescrizione("monocromo ed alcolemia");
			accertamento.setNome("prelievo di sangue");
			accertamento.setTipoAccertamento(ta);
			session.persist(accertamento);

			accertamento = new Accertamento();
			accertamento.setAccId(2);
			accertamento.setCodiceAcc(2);
			accertamento.setDescrizione("canabinoidi, oppiacei, sintetici");
			accertamento.setNome("analisi delle urine");
			accertamento.setTipoAccertamento(ta);

			session.persist(accertamento);

			Set<Ospedale> ospedali = new HashSet<Ospedale>();
			Set<TipoAccertamento> tipiAccertamento = new HashSet<TipoAccertamento>();

			Ospedale o = new Ospedale();
			o.setOspId(1);
			o.setCodiceOsp(1);
			o.setCitta("Bologna");
			o.setNome("S.Orsola");
			o.setIndirizzo("Via Massarenti");
			o.setTipiAccertamento(tipiAccertamento);

			session.persist(o);

			ospedali.add(o);

			o = new Ospedale();
			o.setOspId(2);
			o.setCodiceOsp(2);
			o.setCitta("Bologna");
			o.setNome("Maggiore");
			o.setIndirizzo("Via Saffi");
			o.setTipiAccertamento(tipiAccertamento);
			session.persist(o);

			ospedali.add(o);

			o = new Ospedale();
			o.setOspId(3);
			o.setCodiceOsp(3);
			o.setCitta("Fermo");
			o.setNome("Murri");
			o.setIndirizzo("Via Murri");
			o.setTipiAccertamento(tipiAccertamento);
			session.persist(o);

			ospedali.add(o);
			ta.setOspedali(ospedali);
			session.saveOrUpdate(ta);

			tx.commit();
			session.close();

			session = HibernateUtil.getSessionFactory().openSession();

			String firstQueryResult = "";
			String secondQueryResult = "";

			//Approccio DB-Oriented
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
				secondQueryResult += arr[0] + " " + arr[1] + " " + arr[2] + "\n Numero Accertamenti erogabili: " + arr[3] + "\n";
			}

			/*			
				//Approccio Java-Oriented
				Query firstQuery = session.createQuery("from " + Ospedale.class.getSimpleName() + " where citta = ? and nome = ?");
				firstQuery.setString(0, "Bologna");
				firstQuery.setString(1, "S.Orsola");
				List<Ospedale> ospedaliRes = firstQuery.list();
			
				for (Ospedale os : ospedaliRes) {
					Set<TipoAccertamento> tipiAcc = os.getTipiAccertamento();
					for (TipoAccertamento tipo : tipiAcc) {
						if (tipo.getDescrizione().compareTo("analisi di laboratorio") == 0) {
							Set<Accertamento> accertamenti = tipo.getAccertamenti();
							for (Accertamento acc : accertamenti) {
								firstQueryResult = firstQueryResult + acc.getNome() + "\n";
							}
						}
					}
				}
			
				Query secondQuery = session.createQuery("from " + Ospedale.class.getSimpleName());
				ospedaliRes = secondQuery.list();
				for (Ospedale os : ospedaliRes) {
					secondQueryResult = secondQueryResult + os.getNome() + " " + os.getIndirizzo() + " " + os.getCitta()
							+ "\n Numero Accertamenti erogabili: ";
					Set<TipoAccertamento> tipiAcc = os.getTipiAccertamento();
					int numAccCounter = 0;
					for (TipoAccertamento tipo : tipiAcc) {
						numAccCounter += tipo.getAccertamenti().size();
					}
					secondQueryResult = secondQueryResult + numAccCounter + "\n";
				}
			*/

			PrintWriter pw = new PrintWriter(new FileWriter("Ospedali.txt"));
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
			e1.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
