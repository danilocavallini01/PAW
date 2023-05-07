package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();

	private static final String CREATE_TIPO_ACCERTAMENTO = 
			"	CREATE TABLE tipoAccertamento(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	codiceTipoAccertamento VARCHAR(50) UNIQUE," +
			"	descrizione VARCHAR(50)" +
			")";
	private static final String CREATE_OSPEDALE = 
			"	CREATE TABLE ospedale(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	codiceOspedale VARCHAR(50) UNIQUE," +
			"	name VARCHAR(50)," +
			"	citta VARCHAR(50)," +
			"	indirizzo VARCHAR(50)" +
			")";
	private static final String CREATE_ACCERTAMENTO = 
			"	CREATE TABLE accertamento(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	codiceAccertamento VARCHAR(50) UNIQUE," +
			"	name VARCHAR(50)," +
			"	descrizione VARCHAR(50)," +
			"	idTipoAccertamento INT NOT NULL," +
			"	FOREIGN KEY (idTipoAccertamento) REFERENCES tipoAccertamento(id)" +
			")";
	
	private static final String CREATE_OSPEDALE_TIPOACC = 
			"	CREATE TABLE ospedale_tipoaccertamento(" +
			"	idOspedale INT NOT NULL," +
			"	idTipoAccertamento INT NOT NULL," +
			"	PRIMARY KEY ( idOspedale, idTipoAccertamento ), " +
			"	FOREIGN KEY (idOspedale) REFERENCES ospedale(id), " +
			"	FOREIGN KEY (idTipoAccertamento) REFERENCES tipoAccertamento(id) " +
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
				session.createSQLQuery("DROP TABLE ospedale").executeUpdate();
				session.createSQLQuery("DROP TABLE tipoAccertamento").executeUpdate();
				session.createSQLQuery("DROP TABLE accertamento").executeUpdate();
				session.createSQLQuery("DROP TABLE ospedale_tipoaccertamento").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			session.createSQLQuery(CREATE_TIPO_ACCERTAMENTO).executeUpdate();
			session.createSQLQuery(CREATE_OSPEDALE).executeUpdate();
			session.createSQLQuery(CREATE_ACCERTAMENTO).executeUpdate();
			session.createSQLQuery(CREATE_OSPEDALE_TIPOACC).executeUpdate();

			tx.commit();
		} finally {
			session.close();
		}
	}
}
