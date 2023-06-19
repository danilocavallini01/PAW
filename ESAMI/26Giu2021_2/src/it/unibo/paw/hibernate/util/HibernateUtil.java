package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();
	
	private static final String CREATE_PROGETTO = "CREATE TABLE progetto("
			+ "id INT NOT NULL PRIMARY KEY,"
			+ "codiceProgetto INT NOT NULL UNIQUE,"
			+ "nomeProgetto VARCHAR(50),"
			+ "annoInizio INT,"
			+ "durata VARCHAR(50)"
			+ ")";
	
	private static final String CREATE_WP = "CREATE TABLE work_package("
			+ "id INT NOT NULL PRIMARY KEY,"
			+ "nome VARCHAR(50) NOT NULL UNIQUE,"
			+ "titolo VARCHAR(50),"
			+ "descrizione VARCHAR(50),"
			+ "idProgetto INT NOT NULL REFERENCES progetto(id) )";
	
	private static final String CREATE_PARTNER = "CREATE TABLE partner("
			+ "id INT NOT NULL PRIMARY KEY,"
			+ "siglaPartner VARCHAR(50), "
			+ "nome VARCHAR(50)"
			+ ")";
	
	private static final String CREATE_WP_PARTNER = "CREATE TABLE workpackage_partner(" +
			"	idPartner INT NOT NULL," +
			"	idWorkPackage INT NOT NULL," +
			"	PRIMARY KEY(idPartner, idWorkPackage)," +
			"	FOREIGN KEY (idPartner) REFERENCES partner(id)," +
			"	FOREIGN KEY (idWorkPackage) REFERENCES work_package(id)" +
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
				session.createSQLQuery("DROP TABLE work_package").executeUpdate();
				session.createSQLQuery("DROP TABLE progetto").executeUpdate();
				session.createSQLQuery("DROP TABLE partner").executeUpdate();
				session.createSQLQuery("DROP TABLE workpackage_partner").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			session.createSQLQuery(CREATE_PROGETTO).executeUpdate();
			session.createSQLQuery(CREATE_WP).executeUpdate();
			session.createSQLQuery(CREATE_PARTNER).executeUpdate();
			session.createSQLQuery(CREATE_WP_PARTNER).executeUpdate();
			
			tx.commit();
		} finally {
			session.close();
		}
	}
}
