package es.enolgor.app.auth;

import java.util.HashMap;
import java.util.Map;

import es.enolgor.configuration.Configuration;
import es.enolgor.configuration.Configuration.AuthorizedUser;

public class ConfigBasicAuthenticationProviderImpl implements BasicAuthenticationProvider{
	
	private final Map<String, String> userPwd;
	
	public ConfigBasicAuthenticationProviderImpl(Configuration configuration) {
		this.userPwd = new HashMap<>();
		for(AuthorizedUser au : configuration.getSecurityConfiguration().getAuthorizedUsers()) {
			userPwd.put(au.getName(), au.getPassword());
		}
	}
	
	@Override
	public boolean authenticate(String username, String password) throws AuthenticationException {
		return userPwd.containsKey(username) && userPwd.get(username).equals(password);
	}
}
