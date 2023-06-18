package it.unibo.paw.hibernate;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import it.unibo.paw.hibernate.util.HibernateUtil;

public class HibernateTest {
	
	private static final String line_sp = "----------------------------------------------------------------------------------";

	public static void main(String[] args) {// throws Exception{

		Session session = null;
		Transaction tx = null;

		try {
			HibernateUtil.dropAndCreateTables();
			
			//Persistenza
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// --- Student -----------------------------

			Student student1 = new Student();
			student1.setId(1);
			student1.setFirstName("Luisa");
			student1.setLastName("Verdi");
			Calendar c = Calendar.getInstance();
			c.set(1984, 1, 24);
			student1.setBirthDate(c.getTime());
			session.persist(student1);

			Student student2 = new Student();
			student2.setId(2);
			student2.setFirstName("Anna");
			student2.setLastName("Bruni");
			c = Calendar.getInstance();
			c.set(1983, 4, 13);
			student2.setBirthDate(c.getTime());
			session.persist(student2);

			tx.commit();

			System.out.println();
			System.out.println("Query: elenco di studenti con cognome Verdi");

			// HQL: il nome della tabella è ottenuto tramite il mapping  
			// presente nel file XML relativo alla classe Student
			System.out.println("(versione HQL)");
			Query query = session.createQuery("from " + Student.class.getSimpleName() + " where lastName = ?");

			// SQL: classica query in cui si pecifica il nome della tabella
			// System.out.println("(versione SQL)");
			//Query query = session.createSQLQuery("select * from students where lastName = ?").addEntity(Student.class);

			query.setString(0, "Verdi");
			List<Student> students = query.list();
			System.out.println("students.size() " + students.size());
			Iterator<Student> it = students.iterator();
			while (it.hasNext()) {
				Student studentResult = it.next();
				System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " " + studentResult.getBirthDate());
			}

			// versione Criteria: soluzione completamente object-oriented
			System.out.println();
			System.out.println("(versione Criteria)");
			Criteria criteria = session.createCriteria(Student.class);
			criteria.add(Restrictions.eq("lastName", "Verdi"));
			students = criteria.list();
			System.out.println("students.size() " + students.size());
			for (Student studentResult : students) {
				System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " " + studentResult.getBirthDate());
			}

			// --- Course -----------------------------

			System.out.println();
			System.out.println(line_sp);
			System.out.println();

			tx = session.beginTransaction();

			Course course1 = new Course();
			course1.setId(1);
			course1.setName("Progettazione di Applicazioni Web");
			session.persist(course1);

			Course course2 = new Course();
			course2.setId(2);
			course2.setName("Fondamenti di Informatica T1");
			session.persist(course2);

			tx.commit();

			System.out.println();
			System.out.println("Query: corso con id = 1");

			System.out.println("(versione HQL)");
			query = session.createQuery("from " + Course.class.getSimpleName() + " where id = ?");
			query.setInteger(0, 1);
			Course courseResult = (Course) query.uniqueResult();
			System.out.println(courseResult.getId() + " " + courseResult.getName());

			System.out.println();
			System.out.println("(versione Criteria)");
			criteria = session.createCriteria(Course.class);
			criteria.add(Restrictions.idEq(1));
			courseResult = (Course) criteria.uniqueResult();
			System.out.println(courseResult.getId() + " " + courseResult.getName());

			// --- Courses_Students Mapping -----------------------------
			// Mapping in hibernate AUTOMATICO! (2 java bean , 3 tabelle)

			System.out.println();
			System.out.println(line_sp);
			System.out.println();

			tx = session.beginTransaction();
			student1.getCourses().add(course1);
			session.saveOrUpdate(student1);

			student2.getCourses().add(course1);
			student2.getCourses().add(course2);
			session.saveOrUpdate(student2);
			tx.commit();
			session.close();
			
			//Lettura da DB
			session= HibernateUtil.getSessionFactory().openSession();
			System.out.println();
			System.out.println("Query: studenti del corso con id = 1");

			//HQL
			System.out.println("(versione HQL)");
			query = session.createQuery("from " + Course.class.getSimpleName() + " where id = ?");
			query.setInteger(0, 1);
			courseResult = (Course) query.uniqueResult();
			System.out.println("Il corso " + courseResult.getName() + " è frequentato da:");
			for (Student student : courseResult.getStudents()) {
				System.out.println(student.getFirstName() + " " + student.getLastName() + " " + student.getBirthDate());
			}

			//Criteria
			System.out.println();
			System.out.println("(versione Criteria)");
			criteria = session.createCriteria(Course.class);
			criteria.add(Restrictions.eq("id", 1));
			List<Course> courses = criteria.list();
			System.out.println("coursesResult.size() " + courses.size());
			Iterator<Course> itCourse = courses.iterator();
			while (itCourse.hasNext()) {
				courseResult = itCourse.next();
				System.out.println("Il corso " + courseResult.getName() + " è frequentato da:");
				for (Student student : courseResult.getStudents()) {
					System.out.println(student.getFirstName() + " " + student.getLastName() + " " + student.getBirthDate());
				}
			}

			System.out.println();
			System.out.println("Query: corsi dello studente con id = 1");

			//HQL
			System.out.println("(versione HQL)");
			query = session.createQuery("from " + Student.class.getSimpleName() + " where id = ?");
			query.setInteger(0, 1);
			Student studentResult = (Student) query.uniqueResult();
			System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " frequenta i seguenti corsi:");
			for (Course course : studentResult.getCourses()) {
				System.out.println("" + course.getId() + " " + course.getName());
			}

			//Criteria
			System.out.println();
			System.out.println("(versione Criteria)");
			criteria = session.createCriteria(Student.class);
			criteria.add(Restrictions.eq("id", 1));
			students = criteria.list();
			System.out.println("coursesResult.size() " + students.size());
			it = students.iterator();
			while (it.hasNext()) {
				studentResult = it.next();
				System.out.println(studentResult.getFirstName() + " " + studentResult.getLastName() + " frequenta i seguenti corsi:");
				for (Course course : studentResult.getCourses()) {
					System.out.println("" + course.getId() + " " + course.getName());
				}
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
