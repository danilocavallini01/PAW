package it.unibo.paw.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import it.unibo.paw.hibernate.util.HibernateUtil;

public class Main {

	public static void main(String[] args) {
		
		HibernateUtil.dropAndCreateTables();
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		StringBuilder rs = new StringBuilder();
		StringBuilder rs2 = new StringBuilder();

		Query firstQuery = session.createQuery("from ospedale where citta = ? and nome = ?");

		firstQuery.setString(0, "Bologna");
		firstQuery.setString(1, "S.Orsola");

		/**
		 * - PRIMA QUERY - L’elenco dei nomi degli accertamenti di tipo “analisi di
		 * laboratorio” effettuate al Sant’Orsola di Bologna
		 */

		List<Ospedale> ospedali = firstQuery.list();

		for (Ospedale ospedale : ospedali) {
			for (TipoAccertamento tipoAcc : ospedale.getTipiAccertamento()) {
				if (tipoAcc.getDescrizione().equals("analisi di laboratorio")) {
					rs.append("[");
					for (Accertamento acc : tipoAcc.getAccertamenti()) {
						rs.append(acc.getNome() + " ");
					}
					rs.append("]");
				}
			}
		}

		/**
		 * - SECONDA QUERY - Per ogni Ospedale, restituire: il nome, la città,
		 * l’indirizzo, e il numero totale di “Accertamenti” erogabili
		 */

		Query secondQuery = session.createQuery("from ospedale");
		ospedali = secondQuery.list();

		for (Ospedale ospedale : ospedali) {
			rs2.append("[" + ospedale.getNome() + "|" + ospedale.getCitta() + "|" + ospedale.getIndirizzo()
					+ ospedale.getTipiAccertamento().size() + "]\n");
		}
		
		System.out.println(rs);
		System.out.println(rs2);
	}

}
