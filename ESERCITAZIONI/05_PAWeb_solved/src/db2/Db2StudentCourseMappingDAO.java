package it.unibo.paw.dao.db2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.unibo.paw.dao.CourseDTO;
import it.unibo.paw.dao.CourseStudentMappingDAO;
import it.unibo.paw.dao.StudentDTO;

public class Db2StudentCourseMappingDAO implements CourseStudentMappingDAO {

	// === Costanti letterali per non sbagliarsi a scrivere !!! ============================

	static final String TABLE = "courses_students";

	// -------------------------------------------------------------------------------------

	static final String ID_C = "idCourse";
	static final String ID_S = "idStudent";

	// == STATEMENT SQL ====================================================================

	// INSERT INTO table ( idCourse, idStudent ) VALUES ( ?,? );
	static final String insert = "INSERT " +
			"INTO " + TABLE + " ( " +
			ID_C + ", " + ID_S + " " +
			") " +
			"VALUES (?,?) ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_ids = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_C + " = ? " +
			"AND " + ID_S + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_id_course = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_C + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String read_by_id_student = "SELECT * " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_S + " = ? ";

	// SELECT * FROM table WHERE stringcolumn = ?;
	static String read_all = "SELECT * " +
			"FROM " + TABLE + " ";

	// DELETE FROM table WHERE idcolumn = ?;
	static String delete = "DELETE " +
			"FROM " + TABLE + " " +
			"WHERE " + ID_C + " = ? " +
			"AND " + ID_S + " = ? ";

	// -------------------------------------------------------------------------------------

	// CREATE entrytable ( code INT NOT NULL PRIMARY KEY, ... );
	static String create = "CREATE " +
			"TABLE " + TABLE + " ( " +
			ID_C + " INT NOT NULL, " +
			ID_S + " INT NOT NULL, " +
			"PRIMARY KEY (" + ID_C + "," + ID_S + " ), " +
			"FOREIGN KEY (" + ID_S + ") REFERENCES students(id), " +
			"FOREIGN KEY (" + ID_C + ") REFERENCES courses(id) " +
			") ";
	static String drop = "DROP " +
			"TABLE " + TABLE + " ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String coursesByStudent_query = "SELECT * " +
			"FROM " + TABLE + " CS, courses C " +
			"WHERE CS.idCourse = C.id AND " + ID_S + " = ? ";

	// SELECT * FROM table WHERE idcolumns = ?;
	static String studentsByCourse_query = "SELECT * " +
			"FROM " + TABLE + " CS, students S " +
			"WHERE CS.idStudent = S.id AND " + ID_C + " = ? ";

	// === METODI DAO =========================================================================

	@Override
	public void create(int idCourse, int idStud) {
		Connection conn = Db2DAOFactory.createConnection();
		if (idCourse < 0 || idStud < 0) {
			System.err.println("create(): cannot insert an entry with an invalid id");
			return;
		}
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(insert);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idCourse);
			prep_stmt.setInt(2, idStud);
			prep_stmt.executeUpdate();
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println("create(): failed to insert entry: " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
	}

	@Override
	public boolean delete(int idCourse, int idStudent) {
		boolean result = false;
		if (idCourse < 0 || idStudent < 0) {
			System.err.println("delete(): cannot delete an entry with an invalid id ");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(delete);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, idCourse);
			prep_stmt.setInt(2, idStudent);
			prep_stmt.executeUpdate();
			result = true;
			prep_stmt.close();
		} catch (Exception e) {
			System.err.println(
					"delete(): failed to delete entry with idCourse = " + idCourse + " and idStudent = " + idStudent + ": " + e.getMessage());
			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean createTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(create);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.err.println("createTable(): failed to create table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public boolean dropTable() {
		boolean result = false;
		Connection conn = Db2DAOFactory.createConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(drop);
			result = true;
			stmt.close();
		} catch (Exception e) {
			System.err.println("dropTable(): failed to drop table '" + TABLE + "': " + e.getMessage());
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<CourseDTO> getCoursesByStudent(int id) {
		List<CourseDTO> result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		result = new ArrayList<CourseDTO>();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(coursesByStudent_query);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			while (rs.next()) {
				CourseDTO c = new CourseDTO();
				c.setId(rs.getInt("id"));
				c.setName(rs.getString("name"));
				result.add(c);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

	@Override
	public List<StudentDTO> getStudentsByCourse(int id) {
		List<StudentDTO> result = null;
		if (id < 0) {
			System.err.println("read(): cannot read an entry with a negative id");
			return result;
		}
		Connection conn = Db2DAOFactory.createConnection();
		result = new ArrayList<StudentDTO>();
		try {
			PreparedStatement prep_stmt = conn.prepareStatement(studentsByCourse_query);
			prep_stmt.clearParameters();
			prep_stmt.setInt(1, id);
			ResultSet rs = prep_stmt.executeQuery();
			while (rs.next()) {
				StudentDTO s = new StudentDTO();
				s.setId(rs.getInt("id"));
				s.setFirstName(rs.getString("firstName"));
				s.setLastName(rs.getString("lastName"));
				long date = rs.getDate("birthdate").getTime();
				java.util.Date birthDate = new java.util.Date(date);
				s.setBirthDate(birthDate);
				result.add(s);
			}
			rs.close();
			prep_stmt.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			Db2DAOFactory.closeConnection(conn);
		}
		return result;
	}

}
