package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();

	private static final String CREATE_OSPEDALI = "CREATE TABLE ospedali(" +
			"	ospId INT NOT NULL PRIMARY KEY," +
			"	codiceOsp int NOT NULL UNIQUE," +
			"	nome varchar(50)," +
			"	citta varchar(50)," +
			"	indirizzo varchar(50)" +
			")";
	private static final String CREATE_TIPOACCERTAMENTO = "CREATE TABLE tipoAccertamento(" +
			"	tipoAccId INT NOT NULL PRIMARY KEY," +
			"	codiceTipoAcc int NOT NULL UNIQUE," +
			"	descrizione varchar(255)" +
			")";

	private static final String CREATE_TIPOACCERTAMENTO_OSPEDALE = "CREATE TABLE tipoAccertamento_ospedale(" +
			"	ospId INT NOT NULL," +
			"	tipoAccId INT NOT NULL," +
			"	PRIMARY KEY(ospId, tipoAccId)," +
			"	FOREIGN KEY (ospId) REFERENCES ospedali(ospId)," +
			"	FOREIGN KEY (tipoAccId) REFERENCES tipoAccertamento(tipoAccId)" +
			")";
	private static final String CREATE_ACCERTAMENTI = "CREATE TABLE accertamenti(" +
			"	accID INT NOT NULL PRIMARY KEY," +
			"	codiceAcc int NOT NULL UNIQUE," +
			"	nome varchar(50)," +
			"	descrizione varchar(255)," +
			"	tipoAccId int," +
			"	FOREIGN KEY (tipoAccId) REFERENCES tipoAccertamento(tipoAccId)" +
			")";

	private static SessionFactory initHibernateUtil() {
		try {
			return new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

	public static void dropAndCreateTables() {
		Session session = getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
					
			try {
				session.createSQLQuery("DROP TABLE accertamenti").executeUpdate();
				session.createSQLQuery("DROP TABLE tipoAccertamento_ospedale").executeUpdate();
				session.createSQLQuery("DROP TABLE tipoAccertamento").executeUpdate();
				session.createSQLQuery("DROP TABLE ospedali").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			session.createSQLQuery(CREATE_OSPEDALI).executeUpdate();
			session.createSQLQuery(CREATE_TIPOACCERTAMENTO).executeUpdate();
			session.createSQLQuery(CREATE_TIPOACCERTAMENTO_OSPEDALE).executeUpdate();
			session.createSQLQuery(CREATE_ACCERTAMENTI).executeUpdate();
			
			tx.commit();
		} finally {
			session.close();
		}
	}
}
