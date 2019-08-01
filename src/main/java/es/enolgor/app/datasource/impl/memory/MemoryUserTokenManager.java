package es.enolgor.app.datasource.impl.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.enolgor.app.auth.UserTokenManager;

public class MemoryUserTokenManager implements UserTokenManager {
	
	private Map<String, Set<String>> userTokens;
	
	public MemoryUserTokenManager() {
		this.userTokens = new HashMap<>();
	}
	
	@Override
	public boolean tokenExists(String username, String token) {
		return this.userTokens.containsKey(username) && this.userTokens.get(username).contains(token);
	}

	@Override
	public void revokeToken(String username, String token) {
		if(!this.userTokens.containsKey(username)) return;
		this.userTokens.get(username).remove(token);
	}

	@Override
	public void revokeAllTokens(String username) {
		if(!this.userTokens.containsKey(username)) return;
		this.userTokens.get(username).clear();
	}

	@Override
	public void addToken(String username, String token) {
		if(!this.userTokens.containsKey(username)) {
			this.userTokens.put(username, new HashSet<>());
		}
		this.userTokens.get(username).add(token);
	}

}
