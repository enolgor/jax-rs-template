package es.enolgor.app.datasource.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import es.enolgor.app.datasource.dao.DAOException.AlreadyExistsException;
import es.enolgor.app.datasource.dao.DAOException.InternalException;
import es.enolgor.app.datasource.dao.DAOException.NotFoundException;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.app.model.Pet;
import es.enolgor.utils.ID;

public class MysqlPetProvider implements PetProvider{

	private Connection conn;
	
	public MysqlPetProvider(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public Collection<Pet> list() throws InternalException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(Pet pet) throws InternalException, AlreadyExistsException {
		String id = pet.getId() != null ? pet.getId() : ID.getRandomID();
		pet.setId(id);
		try {
			PreparedStatement stm = conn.prepareStatement(Statements.CREATE_PET_ENTITY);
			stm.setString(1,  pet.getId());
			stm.setString(2,  pet.getName());
			stm.setString(3,  pet.getDateBirth());
			stm.setString(4,  pet.getCurrentDisease().toString());
			stm.executeUpdate();
			return id;
		} catch (SQLException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public Pet read(String id) throws InternalException, NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Pet t) throws InternalException, NotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) throws InternalException, NotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Pet> listPetsByOwner(String ownerId) throws InternalException {
		// TODO Auto-generated method stub
		return null;
	}

}
