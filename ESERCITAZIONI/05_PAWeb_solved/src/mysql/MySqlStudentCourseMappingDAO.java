package it.unibo.paw.dao.mysql;

import java.util.List;

import it.unibo.paw.dao.CourseDTO;
import it.unibo.paw.dao.CourseStudentMappingDAO;
import it.unibo.paw.dao.StudentDTO;

public class MySqlStudentCourseMappingDAO implements CourseStudentMappingDAO {


	
	@Override
	public void create(int idc, int ids){};

	
	@Override
	public boolean delete(int idCourse, int idStudent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createTable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dropTable() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public List<CourseDTO> getCoursesByStudent(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<StudentDTO> getStudentsByCourse(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
