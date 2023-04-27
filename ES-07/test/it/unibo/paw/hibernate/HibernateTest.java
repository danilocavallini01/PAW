package it.unibo.paw.hibernate;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import it.unibo.paw.hibernate.util.HibernateUtil;

public class HibernateTest {

	public static void main(String[] args) {// throws Exception{

		Session session = null;
		Transaction tx = null;

		try {
			HibernateUtil.dropAndCreateTables();

			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// COURSE 1

			Course course = new Course();
			course.setId(1);
			course.setName("PAW");
			
			session.persist(course);
			
			// STUDENT 1
			Student student = new Student();
			student.setId(1);
			student.setFirstName("Marco");
			student.setLastName("Rossi");
			Calendar c = Calendar.getInstance();
			c.set(1984, 1, 24);
			student.setBirthDate(c.getTime());
		    student.addCourse(course);			
		 	    
			session.persist(student);

			// STUDENT 2
			student = new Student();
			student.setId(2);
			student.setFirstName("Giovanni");
			student.setLastName("Gialli");
			c = Calendar.getInstance();
			c.set(1983, 4, 13);
			student.setBirthDate(c.getTime());
			student.addCourse(course);		
			
			session.persist(student);

			tx.commit();

			// richiedo l'elenco degli studenti che hanno cognome "Gialli"
			// versione SQL/HQL
			System.out.println();
			System.out.println("query students: sql/hql");

			// query HQL: il nome della tabella è ottenuto tramite il mapping  
			// presente nel file XML relativo alla classe Student
			Query query = session.createQuery("from " + Student.class.getSimpleName() + " where lastName = ?");

			// variante SQL: classica query in cui si pecifica il nome della tabella
			//Query query = session.createSQLQuery("select * from students where lastName = ?").addEntity(Student.class);

			query.setString(0, "Gialli");
			List<Student> students = query.list();
			System.out.println("students.size() " + students.size());
			Iterator<Student> it = students.iterator();
			while (it.hasNext()) {
				Student studentResult = it.next();
				System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " " 
				+ studentResult.getBirthDate() + " " + student.getCourses());
			}

			// versione Criteria: soluzione completamente object-oriented
			System.out.println();
			System.out.println("query students: criteria");
			Criteria criteria = session.createCriteria(Student.class);
			criteria.add(Restrictions.eq("lastName", "Gialli"));
			students = criteria.list();
			System.out.println("students.size() " + students.size());
			for (Student studentResult : students) {
				System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " " 
						+ studentResult.getBirthDate() + " " + student.getCourses());
			}

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
