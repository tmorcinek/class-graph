package pl.edu.agh.morcinek.logic;

import java.util.Date;

public class Osoba implements OsobaInterface{
	
	private String name;
	
	private String surname;
	
	private Integer id;
	
	private Date birthDate;	
	
	private Date dieDate;
	
	
	public Osoba(String name, String surname, Integer id, Date birthDate,
			Date dieDate) {
		this.name = name;
		this.surname = surname;
		this.id = id;
		this.birthDate = birthDate;
		this.dieDate = dieDate;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public Date getDieDate() {
		return dieDate;
	}


	public void setDieDate(Date dieDate) {
		this.dieDate = dieDate;
	}
	
	
}
