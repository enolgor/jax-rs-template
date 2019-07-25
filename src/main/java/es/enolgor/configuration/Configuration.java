package es.enolgor.configuration;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "configuration")
public class Configuration {
	
	@Element( name = "database")
	private DatabaseConfiguration databaseConfiguration;
	public DatabaseConfiguration getDatabaseConfiguration(){ return databaseConfiguration; }
	
	@Element( name = "security")
	private SecurityConfiguration securityConfiguration;
	public SecurityConfiguration getSecurityConfiguration(){ return securityConfiguration; }
	
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
	
	@Root(name = "security")
	public static class SecurityConfiguration {
		@Element
		private int tokenDurationSeconds;
		public int getTokenDurationSeconds() { return tokenDurationSeconds; }
		
		@ElementList
		private List<AuthorizedUser> authorizedUsers;
		public List<AuthorizedUser> getAuthorizedUsers() { return authorizedUsers; }
	}
	
	@Root(name = "user")
	public static class AuthorizedUser {
		@Attribute
		private String name;
		public String getName() { return name; }
		
		@Attribute
		private String password;
		public String getPassword() { return password; }
	}
}
