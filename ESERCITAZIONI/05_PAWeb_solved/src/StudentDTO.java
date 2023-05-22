package it.unibo.paw.dao;

import java.io.Serializable;
import java.util.*;
import java.util.Date;

public class StudentDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	// ---------------------------
	
	private int id;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private List<CourseDTO> courses;
	private boolean alreadyLoaded;
	
	
	// --- constructor ----------
	
	public StudentDTO() {
		courses = new ArrayList<CourseDTO>();
		alreadyLoaded = false;
	}
	
	
	// --- getters and setters --------------
	
	
	
	
	public int getId() {
		return id;
	}

	public boolean isAlreadyLoaded()
	{
		return this.alreadyLoaded;
	}
	
	public void isAlreadyLoaded(boolean loaded)
	{
		this.alreadyLoaded=loaded;
	}


	public List<CourseDTO> getCourses() {
		return courses;
	}


	public void setCourses(List<CourseDTO> courses) {
		this.courses = courses;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	

	// --- utilities ----------------------------
	
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
		StudentDTO other = (StudentDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

}
