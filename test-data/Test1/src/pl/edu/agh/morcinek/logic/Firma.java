package pl.edu.agh.morcinek.logic;

import java.util.List;

public class Firma {

	static private String nazwa;
	
	String adres;
	
	protected Integer zysk;
	
	private List<Pracownik> listaPracownikow;

	public Firma(String nazwa, String adres) {
		super();
		this.nazwa = nazwa;
		this.adres = adres;
	}

	public synchronized String getNazwa() {
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

	public Integer getZysk() {
		return zysk;
	}

	public void setZysk(Integer zysk) {
		this.zysk = zysk;
	}

	public List<Pracownik> getListaPracownikow() {
		return listaPracownikow;
	}

	public void addPracownik(Pracownik pracownik) {
		this.listaPracownikow.add(pracownik);
	}
	
}
