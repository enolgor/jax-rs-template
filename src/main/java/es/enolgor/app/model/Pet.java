package es.enolgor.app.model;

@SuppressWarnings("serial")
public class Pet extends IdentifiableObject{
	private String name;
	private String dateBirth;
	private Disease currentDisease;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateBirth() {
		return dateBirth;
	}
	public void setDateBirth(String dateBirth) {
		this.dateBirth = dateBirth;
	}
	public Disease getCurrentDisease() {
		return currentDisease;
	}
	public void setCurrentDisease(Disease currentDisease) {
		this.currentDisease = currentDisease;
	}

}
