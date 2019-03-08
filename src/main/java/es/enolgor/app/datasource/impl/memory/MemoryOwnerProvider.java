package es.enolgor.app.datasource.impl.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import es.enolgor.app.datasource.dao.DAOException.AlreadyExistsException;
import es.enolgor.app.datasource.dao.DAOException.NotFoundException;
import es.enolgor.app.datasource.dao.providers.OwnerProvider;
import es.enolgor.app.model.Owner;
import es.enolgor.utils.ID;

public class MemoryOwnerProvider implements OwnerProvider{

	private Map<String, Owner> owners;
	
	public MemoryOwnerProvider() {
		this.owners = new HashMap<>();
	}
	
	@Override
	public synchronized Collection<Owner> list() {
		return owners.values();
	}

	@Override
	public synchronized Owner read(String id) throws NotFoundException {
		if(!owners.containsKey(id)) throw new NotFoundException(Owner.class, id);
		return owners.get(id);
	}

	@Override
	public synchronized String create(Owner owner) throws AlreadyExistsException {
		String id = owner.getId() != null ? owner.getId() : ID.getRandomID();
		owner.setId(id);
		if(owners.containsKey(id)) throw new AlreadyExistsException(owner);
		owners.put(id, owner);
		return id;
	}

	@Override
	public synchronized void update(Owner owner) throws NotFoundException {
		String id = owner.getId();
		if(!owners.containsKey(id)) throw new NotFoundException(owner);
		owners.put(id, owner);
	}

	@Override
	public synchronized void delete(String id) throws NotFoundException {
		if(!owners.containsKey(id)) throw new NotFoundException(Owner.class, id);
		owners.remove(id);
	}

	@Override
	public Collection<Owner> searchOwnerByName() {
		// TODO Auto-generated method stub
		return null;
	}

}
