package es.enolgor.app;

import java.nio.file.Paths;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import es.enolgor.app.datasource.DataSource;
import es.enolgor.app.datasource.impl.memory.MemoryDataSource;
import es.enolgor.configuration.Configuration;
import es.enolgor.configuration.ConfigurationProvider;

public class AppBinder extends AbstractBinder{

	@Override
	protected void configure() {
		Configuration configuration;
		DataSource dataSource;
		
		try {
			
			Configuration defaultConfiguration = ConfigurationProvider.readConfigurationFromStream(App.class.getResourceAsStream("/default.configuration.xml"));
			configuration = ConfigurationProvider.readConfigurationFromFile(Paths.get(AppProperties.getAppConfigurationDir(), "configuration.xml").toString(), defaultConfiguration);
			
			//dataSource = new MysqlDataSource();
			dataSource = new MemoryDataSource();
			
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
		bind(configuration).to(Configuration.class);
		bind(dataSource).to(DataSource.class);
	}
	
}
