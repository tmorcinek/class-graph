package pl.edu.agh.morcinek.logic;

import java.util.Date;

public class Student extends Osoba{

	private Integer studentId;
	
	private Integer year;
	
	static private Double avarage;
	

	public Student(String name, String surname, Integer id, Date birthDate,
			Date dieDate) {
		super(name, surname, id, birthDate, dieDate);
		// TODO Auto-generated constructor stub
	}


	public Integer getStudentId() {
		return studentId;
	}


	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}


	public Integer getYear() {
		return year;
	}


	public void setYear(Integer year) {
		this.year = year;
	}


	public Double getAvarage() {
		return avarage;
	}


	public void setAvarage(Double avarage) {
		this.avarage = avarage;
	}

	
	
}
