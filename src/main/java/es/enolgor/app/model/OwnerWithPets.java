package es.enolgor.app.model;

@SuppressWarnings("serial")
public class OwnerWithPets extends Owner{
	private Pet [] pets;

	public Pet[] getPets() {
		return pets;
	}

	public void setPets(Pet[] pets) {
		this.pets = pets;
	}
	
	
}
