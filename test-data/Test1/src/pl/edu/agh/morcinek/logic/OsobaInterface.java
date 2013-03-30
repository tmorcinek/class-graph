package pl.edu.agh.morcinek.logic;

import java.util.Date;

public interface OsobaInterface {

	public String getName();
	
	public void setName(String name);

	public String getSurname();

	public void setSurname(String surname);

	public Integer getId();

	public void setId(Integer id);
	
	public Date getBirthDate();

	public void setBirthDate(Date birthDate);

	public Date getDieDate();

	public void setDieDate(Date dieDate);
}
