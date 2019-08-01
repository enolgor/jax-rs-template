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
	
	@Element( name = "server")
	private ServerConfiguration serverConfiguration;
	public ServerConfiguration getServerConfiguration(){ return serverConfiguration; }
	
	@Root(name = "database")
	public static class DatabaseConfiguration {
		
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
		private int accessTokenDurationSeconds;
		public int getAccessTokenDurationSeconds() { return accessTokenDurationSeconds; }
		
		@Element
		private int refreshTokenDurationDays;
		public int getRefreshTokenDurationDays() { return refreshTokenDurationDays; }
		
		@Element
		private String jwtSecret;
		public String getJWTSecret() { return jwtSecret; }
		
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
	
	@Root(name = "server")
	public static class ServerConfiguration {
		@ElementList
		private List<Header> headers;
		public List<Header> getHeaders() { return headers; }
	}
	
	@Root(name = "header")
	public static class Header {
		@Attribute
		private boolean enabled;
		public boolean isEnabled() { return enabled; }
		
		@Attribute
		private String key;
		public String getKey() { return key; }
		
		@Attribute
		private String value;
		public String getValue() { return value; }
	}
}
