package es.enolgor.app;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.App.ApplicationListenerAdapter;
import es.enolgor.app.datasource.DataSource;
import es.enolgor.configuration.Configuration;

public class AppListener extends ApplicationListenerAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(AppListener.class);
	
	@Inject DataSource dataSource;
	@Inject Configuration configuration;
	
	@Override
	public void onApplicationInit() {
		try {
			dataSource.onInit(configuration);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		logger.info("Initialized");
	}

	@Override
	public void onApplicationDestroy() {
		try{
			dataSource.onClose();
			logger.info("Database closed.");
		}catch(Exception e){
			logger.error("Database stop error: ",e);
			throw new RuntimeException(e);
		}
	}

}
