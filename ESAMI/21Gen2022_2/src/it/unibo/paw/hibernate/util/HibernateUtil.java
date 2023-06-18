package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();
	
	private static final String CREATE_VOLI = "CREATE TABLE voloAeroportoBo("
			+ " id INT NOT NULL PRIMARY KEY,"
			+ " codVolo INT NOT NULL UNIQUE,"
			+ " compagniaAerea VARCHAR(50) NOT NULL,"
			+ " localitaDestinazione VARCHAR(50) NOT NULL, "
			+ " dataPartenza DATE NOT NULL, "
			+ " orarioPartenza TIME NOT NULL )";
	
	private static final String CREATE_PASSEGGERI = "CREATE TABLE passeggero("
			+ "id INT NOT NULL PRIMARY KEY,"
			+ "codPasseggero INT NOT NULL UNIQUE,"
			+ "nome VARCHAR(50) NOT NULL,"
			+ "cognome VARCHAR(50) NOT NULL,"
			+ "numPassaporto VARCHAR(50) NOT NULL)";
	
	private static final String CREATE_VOLI_PASSEGGERI = "CREATE TABLE passeggeri_voli("
			+ "idPasseggero INT NOT NULL,"
			+ "idVolo INT NOT NULL,"
			+ "PRIMARY KEY ( idPasseggero, idVolo ), "
			+ "FOREIGN KEY (idVolo) REFERENCES voloAeroportoBo(id), "
			+ "FOREIGN KEY (idPasseggero) REFERENCES passeggero(id) )";

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
				session.createSQLQuery("DROP TABLE passeggero").executeUpdate();
				session.createSQLQuery("DROP TABLE voloAeroportoBo").executeUpdate();
				session.createSQLQuery("DROP TABLE passeggeri_voli").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			
			session.createSQLQuery(CREATE_PASSEGGERI).executeUpdate();
			session.createSQLQuery(CREATE_VOLI).executeUpdate();
			session.createSQLQuery(CREATE_VOLI_PASSEGGERI).executeUpdate();

			tx.commit();
		} finally {
			session.close();
		}
	}
}
