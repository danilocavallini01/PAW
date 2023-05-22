package it.unibo.paw.dao;


import java.util.List;

public interface CourseStudentMappingDAO {

	// --- CRUD -------------


	
	public void create(int idCourse, int idStudent);



	public boolean delete(int idCourse, int idStudent);

	
	// ----------------------------------




	// ----------------------------------

	
	public boolean createTable();

	public boolean dropTable();

	public List<CourseDTO> getCoursesByStudent(int id);

	public List<StudentDTO> getStudentsByCourse(int id);

}
