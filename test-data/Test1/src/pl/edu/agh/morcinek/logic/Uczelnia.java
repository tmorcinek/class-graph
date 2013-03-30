package pl.edu.agh.morcinek.logic;

import java.util.Date;
import java.util.List;

public class Uczelnia {

	private String nazwa;
	
	private String adres;

	private List<Nauczyciel> nauczyciele;
	
	private List<Student> studenci;
	
	private List<Pracownik> pracownicy;

	public Uczelnia(String nazwa, String adres) {
		super();
		this.nazwa = nazwa;
		this.adres = adres;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}

	public String getAdres() {
		return adres;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public List<Nauczyciel> getNauczyciele() {
		return nauczyciele;
	}

	public void addNauczyciele(Nauczyciel nauczyciel) {
		this.nauczyciele.add(nauczyciel);
	}

	public List<Student> getStudenci() {
		return studenci;
	}

	public void addStudenci(Student student) {
		this.studenci.add(student);
	}

	public List<Pracownik> getPracownicy() {
		return pracownicy;
	}

	public void addPracownicy(String name, String surname, Integer id, Date birthDate,
			Date dieDate, Integer salary, Integer nip) {
		
		Pracownik pracownik = new Pracownik(name, surname, id, birthDate, dieDate, salary, nip);
		this.pracownicy.add(pracownik);
	}
	
	
	
}
