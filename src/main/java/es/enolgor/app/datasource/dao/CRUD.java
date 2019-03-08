package es.enolgor.app.datasource.dao;

import java.util.Collection;

import es.enolgor.app.datasource.dao.DAOException.AlreadyExistsException;
import es.enolgor.app.datasource.dao.DAOException.InternalException;
import es.enolgor.app.datasource.dao.DAOException.NotFoundException;
import es.enolgor.app.model.IdentifiableObject;

public interface CRUD<T extends IdentifiableObject> {
	
	public Collection<T> list() throws InternalException;
	
	public String create(T t) throws InternalException, AlreadyExistsException;
	public T read(String id) throws InternalException, NotFoundException;
	public void update(T t) throws InternalException, NotFoundException;
	public void delete(String id) throws InternalException, NotFoundException;
}
