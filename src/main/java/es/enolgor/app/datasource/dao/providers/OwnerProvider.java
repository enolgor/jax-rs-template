package es.enolgor.app.datasource.dao.providers;

import java.util.Collection;

import es.enolgor.app.datasource.dao.CRUD;
import es.enolgor.app.datasource.dao.DAOException.InternalException;
import es.enolgor.app.model.Owner;

public interface OwnerProvider extends CRUD<Owner>{
	public Collection<Owner> searchOwnerByName() throws InternalException;
}
