package es.enolgor.app.datasource.dao.providers;

import java.util.Collection;

import es.enolgor.app.datasource.dao.CRUD;
import es.enolgor.app.datasource.dao.DAOException.InternalException;
import es.enolgor.app.model.Pet;

public interface PetProvider extends CRUD<Pet>{
	public Collection<Pet> listPetsByOwner(String ownerId) throws InternalException;
}
