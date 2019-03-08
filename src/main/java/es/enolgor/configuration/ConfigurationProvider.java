package es.enolgor.configuration;

import java.io.File;
import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationProvider.class);
	
	private static final Serializer serializer = new Persister();
	
	public static Configuration readConfigurationFromStream(InputStream is) throws ConfigurationException {
		return ConfigurationProvider.readConfigurationFromStream(is, null);
	}
	
	public static Configuration readConfigurationFromStream(InputStream is, Configuration defaultConfiguration) throws ConfigurationException{
		Configuration config;
		try {
			config = serializer.read(Configuration.class, is);
		}catch(Exception ex) {
			config = defaultConfiguration;
			if(config == null) {
				throw new ConfigurationException(String.format("Exception thrown while reading configuration: %s. No default configuration provided.", ex.getMessage()));
			} else {
				logger.warn(String.format("Exception thrown while reading configuration: %s. Using default configuration.", ex.getMessage()));
			}
		}
		return config;
	}
	
	public static Configuration readConfigurationFromFile(String filepath) throws ConfigurationException {
		return ConfigurationProvider.readConfigurationFromFile(filepath, null);
	}
	
	public static Configuration readConfigurationFromFile(String filepath, Configuration defaultConfiguration) throws ConfigurationException {
		// String configuration_filepath = System.getProperty("application.data.path")+"/"+System.getProperty("application.name")+".cfg.xml";
		File file = new File(filepath);
		Configuration config;
		if(file.exists()){
			try {
				config = serializer.read(Configuration.class, file);
			} catch (Exception e) {
				throw new ConfigurationException(String.format("Exception throw while reading configuration file in %s. %s", filepath, e.getMessage()));
			}
		}else{
			config = defaultConfiguration;
			if(config == null) {
				throw new ConfigurationException(String.format("File configuration not found: %s. No default configuration provided.", filepath));
			} else {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
					serializer.write(config, file);
					logger.info(String.format("Configuration file not found. New file created using default configuration: %s", filepath));
				}catch(Exception ex) {
					throw new ConfigurationException(String.format("Exception thrown while writting default configuration to file %s. %s", filepath, ex.getMessage()));
				}
			}
		}
		return config;
	}
	
	@SuppressWarnings("serial")
	public static class ConfigurationException extends Exception {
		public ConfigurationException(String message) {
			super(message);
		}
		@Override
		public String getMessage() {
			return super.getMessage();
		}
	}
	
}
