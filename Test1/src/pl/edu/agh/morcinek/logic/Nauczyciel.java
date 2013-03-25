package pl.edu.agh.morcinek.logic;

import java.util.Date;

public class Nauczyciel extends Osoba{
	
	private Integer salary;
	
	private String title;
	


	public Nauczyciel(String name, String surname, Integer id, Date birthDate,
			Date dieDate) {
		super(name, surname, id, birthDate, dieDate);
		// TODO Auto-generated constructor stub
	}
	
	
	public Integer getSalary() {
		return salary;
	}


	public void setSalary(Integer salary) {
		this.salary = salary;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


}
