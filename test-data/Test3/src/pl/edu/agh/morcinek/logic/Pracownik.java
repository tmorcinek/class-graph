package pl.edu.agh.morcinek.logic;

import java.util.Date;

public class Pracownik extends Osoba{

	private Integer salary;
	
	private Integer nip;
	
	
	public Pracownik(String name, String surname, Integer id, Date birthDate,
			Date dieDate, Integer salary, Integer nip) {
		super(name, surname, id, birthDate, dieDate);
		// TODO Auto-generated constructor stub
		this.salary = salary;
		this.nip = nip;
	}


	public Integer getSalary() {
		return salary;
	}


	public void setSalary(Integer salary) {
		this.salary = salary;
	}


	public Integer getNip() {
		return nip;
	}


	public void setNip(Integer nip) {
		this.nip = nip;
	}
	
	private class Zawod{
		
		private String name;
		private Integer degree;
		private Integer retiringAge;
		
		public String getName() {
			return name;
		}
		public Integer getDegree() {
			return degree;
		}
		public Integer getRetiringAge() {
			return retiringAge;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setDegree(Integer degree) {
			this.degree = degree;
		}
		public void setRetiringAge(Integer retiringAge) {
			this.retiringAge = retiringAge;
		}
	}
}
