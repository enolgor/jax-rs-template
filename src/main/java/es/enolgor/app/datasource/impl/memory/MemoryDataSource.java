package es.enolgor.app.datasource.impl.memory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.datasource.DataSource;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.configuration.Configuration;

public class MemoryDataSource implements DataSource{
	
	private static final Logger logger = LoggerFactory.getLogger(MemoryDataSource.class);

	private MemoryPetProvider memoryPetProvider;
	
	@Override
	public PetProvider getPetProvider() {
		return memoryPetProvider;
	}

	@Override
	public void onInit(Configuration configuration) {
		logger.info("Initializing Memory DAO");
		this.memoryPetProvider = new MemoryPetProvider();
	}

	@Override
	public void onClose() {
		logger.info("Destroying Memory DAO...");
	}

}
