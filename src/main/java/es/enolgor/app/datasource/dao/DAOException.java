package es.enolgor.app.datasource.dao;

import es.enolgor.app.model.IdentifiableObject;

@SuppressWarnings("serial")
public class DAOException extends Exception{
	
	public DAOException(String message){
		super(message);
	}
	
	public final static class NotFoundException extends DAOException{
		
		public NotFoundException(IdentifiableObject o) {
			this(o.getClass(), o.getId());
		}
		
		public NotFoundException(Class<?> clss, String id) {
			super(String.format("Object of class: %s with id: %s not found", clss.getName(), id));
		}
	
	}
	
	public final static class AlreadyExistsException extends DAOException{
		
		public AlreadyExistsException(IdentifiableObject o) {
			this(o.getClass(), o.getId());
		}
		
		public AlreadyExistsException(Class<?> clss, String id) {
			super(String.format("Object of class: %s with id: %s already exists", clss.getName(), id));
		}
	
	}
	
	public final static class InternalException extends DAOException{
		
		public InternalException(String message) {
			super(message);
		}
	
	}
	
}
