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
	
	private static final String CREATE_OGGETTI = "CREATE TABLE oggetto_digitale(" +
			"id INT NOT NULL PRIMARY KEY," + 
			"codiceOggetto INT NOT NULL UNIQUE," + 
			"nome varchar(50)," +
			"formato varchar(50)," +
			"dataDigitalizzaione DATE," +
			"materiale_id INT NOT NULL" +
			"FOREIGN KEY (materiale_id) REFERENCES materiale_fisico(id)";
	
	private static final String CREATE_MATERIALE = "CREATE TABLE materiale_fisico(" +
			"id INT NOT NULL PRIMARY KEY," + 
			"codiceMateriale INT NOT NULL UNIQUE," + 
			"nome varchar(50)," +
			"descrizione varchar(50)," +
			"dataCreazione DATE," +
			"archivio_id INT NOT NULL" +
			"FOREIGN KEY (archivio_id) REFERENCES archivio_fisico(id)";
	
	private static final String CREATE_ARCHIVIO = "CREATE TABLE archivio_fisico(" +
			"id INT NOT NULL PRIMARY KEY," + 
			"codiceArchivio INT NOT NULL UNIQUE," + 
			"nome varchar(50)," +
			"descrizione varchar(50)," +
			"dataCreazione DATE";

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
				session.createSQLQuery("DROP TABLE oggetto_digitale").executeUpdate();
				session.createSQLQuery("DROP TABLE materiale_fisico").executeUpdate();
				session.createSQLQuery("DROP TABLE archivio_fisico").executeUpdate();

			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			session.createSQLQuery(CREATE_ARCHIVIO).executeUpdate();
			session.createSQLQuery(CREATE_MATERIALE).executeUpdate();
			session.createSQLQuery(CREATE_OGGETTI).executeUpdate();
			
			tx.commit();
		} finally {
			session.close();
		}
	}
}
