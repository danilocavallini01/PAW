package it.unibo.paw.hibernate.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static SessionFactory sessionFactory = initHibernateUtil();

	private static final String CREATE_STUDENTS = "CREATE TABLE students(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	firstName VARCHAR(50)," +
			"	lastName VARCHAR(50)," +
			"	birthDate DATE" +
			")";
	private static final String CREATE_COURSES = "CREATE TABLE courses(" +
			"	id INT NOT NULL PRIMARY KEY," +
			"	name VARCHAR(50)" +
			")";
	private static final String CREATE_COURSES_STUDENTS = "CREATE TABLE courses_students(" +
			"	idCourse INT NOT NULL," +
			"	idStudent INT NOT NULL," +
			"PRIMARY KEY ( idCourse, idStudent ), " +
			"FOREIGN KEY (idStudent) REFERENCES students(id), " +
			"FOREIGN KEY (idCourse) REFERENCES courses(id) " +
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
				session.createSQLQuery("DROP TABLE students").executeUpdate();
				session.createSQLQuery("DROP TABLE courses").executeUpdate();
				session.createSQLQuery("DROP TABLE courses_students").executeUpdate();
			} catch (HibernateException e) {
				System.out.println("dropTable(): failed to drop tables " + e.getMessage());
			}
			session.createSQLQuery(CREATE_STUDENTS).executeUpdate();
			session.createSQLQuery(CREATE_COURSES).executeUpdate();
			session.createSQLQuery(CREATE_COURSES_STUDENTS).executeUpdate();

			tx.commit();
		} finally {
			session.close();
		}
	}
}
