package es.enolgor.app.datasource.impl.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.auth.UserTokenManager;
import es.enolgor.app.datasource.DataSource;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.configuration.Configuration;

public class MemoryDataSource implements DataSource{
	
	private static final Logger logger = LoggerFactory.getLogger(MemoryDataSource.class);

	private MemoryPetProvider memoryPetProvider;
	private MemoryUserTokenManager memoryUserTokenManager;
	
	@Override
	public PetProvider getPetProvider() {
		return memoryPetProvider;
	}
	
	@Override
	public UserTokenManager getUserTokenManager() {
		return memoryUserTokenManager;
	}

	@Override
	public void onInit(Configuration configuration) {
		logger.info("Initializing Memory DAO");
		this.memoryPetProvider = new MemoryPetProvider();
		this.memoryUserTokenManager = new MemoryUserTokenManager();
	}

	@Override
	public void onClose() {
		logger.info("Destroying Memory DAO...");
	}

}
