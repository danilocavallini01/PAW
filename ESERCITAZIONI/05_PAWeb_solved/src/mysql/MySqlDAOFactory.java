package it.unibo.paw.dao.mysql;

import java.sql.Connection;
import java.sql.DriverManager;

import it.unibo.paw.dao.CourseDAO;
import it.unibo.paw.dao.CourseStudentMappingDAO;
import it.unibo.paw.dao.DAOFactory;
import it.unibo.paw.dao.StudentDAO;

public class MySqlDAOFactory extends DAOFactory {

	/**
	 * Name of the class that holds the jdbc driver implementation for the MySQL database
	 */
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	
	/**
	 * URI of the database to connect to
	 */
	public static final String DBURL = "jdbc:mysql://localhost:3306/tw_stud";

	public static final String USERNAME = "xxx";
	public static final String PASSWORD = "xxx";

	// --------------------------------------------

	static {
		try {
			Class.forName(DRIVER).newInstance();
		} 
		catch (Exception e) {
			System.err.println(MySqlDAOFactory.class.getName()+": failed to load MySQL JDBC driver\n"+e);
			e.printStackTrace();
		}
	}

	// --------------------------------------------
	
	public static Connection createConnection() {
		try {
			Connection conn = DriverManager.getConnection (DBURL, USERNAME, PASSWORD);
			System.out.println(MySqlDAOFactory.class.getName()+".createConnection(): database connection established");
			return conn;
		} 
		catch (Exception e) {
			System.err.println(MySqlDAOFactory.class.getName()+".createConnection(): failed creating connection\n"+e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		}
		catch (Exception e) {
			System.err.println(MySqlDAOFactory.class.getName()+".closeConnection(): failed closing connection\n"+e);
			e.printStackTrace();
		}
	}

	// --------------------------------------------
	
	@Override
	public StudentDAO getStudentDAO() {
		return new MySqlStudentDAO();
	}

	@Override
	public CourseDAO getCourseDAO() {
		return new MySqlCourseDAO();
	}
	
	@Override
	public CourseStudentMappingDAO getStudentCourseMappingDAO() {
		return new MySqlStudentCourseMappingDAO();
	}
	
}
