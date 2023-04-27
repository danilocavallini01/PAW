package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();

	private static final String CREATE_STUDENT = "CREATE TABLE students(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	firstName VARCHAR(50)," +
			"	lastName VARCHAR(50)," +
			"	birthDate DATE" +
			")";
	
	private static final String CREATE_COURSE = "CREATE TABLE courses(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	name VARCHAR(50)" +
			")";
	
	private static final String CREATE_COURSE_STUDENT = "CREATE TABLE course_student(" +
			"	idCourse INT NOT NULL," +
			"	idStudent INT NOT NULL, " +
			"   PRIMARY KEY(idCourse,idStudent)" +
			")";

	private static SessionFactory initHibernateUtil() {
		try {
			//return new Configuration().configure().buildSessionFactory();
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
				session.createSQLQuery("DROP TABLE students").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			try {
				session.createSQLQuery("DROP TABLE courses").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			try {
				session.createSQLQuery("DROP TABLE course_student").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			
			session.createSQLQuery(CREATE_STUDENT).executeUpdate();
			session.createSQLQuery(CREATE_COURSE).executeUpdate();
			session.createSQLQuery(CREATE_COURSE_STUDENT).executeUpdate();
			
			tx.commit();
		} finally {
			session.close();
		}
	}
}
