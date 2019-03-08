package es.enolgor.configuration;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "configuration")
public class Configuration {
	
	@Element( name = "database")
	private DatabaseConfiguration databaseConfiguration;
	public DatabaseConfiguration getDatabaseConfiguration(){ return databaseConfiguration; }
	
	@Root(name = "database")
	public static class DatabaseConfiguration{
		
		@Element
		private String url;
		public String getUrl(){ return url; }
		
		@Element
		private String username;
		public String getUsername(){ return username; }
		
		@Element
		private String password;
		public String getPassword(){ return password; }
	}
}
