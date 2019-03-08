package es.enolgor.app.datasource.impl.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.enolgor.app.datasource.dao.DAOException.AlreadyExistsException;
import es.enolgor.app.datasource.dao.DAOException.NotFoundException;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.app.model.Pet;
import es.enolgor.utils.ID;

public class MemoryPetProvider implements PetProvider{
	
	private Map<String, Pet> pets;
	
	public MemoryPetProvider() {
		this.pets = new HashMap<>();
	}
	
	@Override
	public synchronized Collection<Pet> list() {
		return pets.values();
	}

	@Override
	public synchronized Pet read(String id) throws NotFoundException {
		if(!pets.containsKey(id)) throw new NotFoundException(Pet.class, id);
		return pets.get(id);
	}

	@Override
	public synchronized String create(Pet pet) throws AlreadyExistsException {
		String id = pet.getId() != null ? pet.getId() : ID.getRandomID();
		pet.setId(id);
		if(pets.containsKey(id)) throw new AlreadyExistsException(pet);
		pets.put(id, pet);
		return id;
	}

	@Override
	public synchronized void update(Pet pet) throws NotFoundException {
		String id = pet.getId();
		if(!pets.containsKey(id)) throw new NotFoundException(pet);
		pets.put(id, pet);
	}

	@Override
	public synchronized void delete(String id) throws NotFoundException {
		if(!pets.containsKey(id)) throw new NotFoundException(Pet.class, id);
		pets.remove(id);
	}

	@Override
	public synchronized Collection<Pet> listPetsByOwner(String ownerId) {
		return pets.values();
	}

}
