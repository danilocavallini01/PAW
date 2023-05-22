package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.*;

public class CourseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	// ---------------------------
	
	private int id;
	private String name;
	private List<StudentDTO> students;
	private boolean alreadyLoaded;
	
	
	// --- constructor ----------
	
	public CourseDTO() {
		this.students = new ArrayList<StudentDTO>();
		this.alreadyLoaded = false;
	}
	
	
	// --- getters and setters --------------
	
	public boolean isAlreadyLoaded()
	{
		return this.alreadyLoaded;
	}
	
	public void isAlreadyLoaded(boolean loaded)
	{
		this.alreadyLoaded=loaded;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	

	// --- utilities ----------------------------
	
	public List<StudentDTO> getStudents() {
		return students;
	}


	public void setStudents(List<StudentDTO> students) {
		this.students = students;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseDTO other = (CourseDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}


}
