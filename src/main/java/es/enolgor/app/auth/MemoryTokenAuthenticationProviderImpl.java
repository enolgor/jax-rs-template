package es.enolgor.app.auth;

import java.util.HashMap;
import java.util.Map;

public class MemoryTokenAuthenticationProviderImpl implements TokenAuthenticationProvider{
	
	private final Map<String, UserToken> userTokens;
	
	public MemoryTokenAuthenticationProviderImpl() {
		this.userTokens = new HashMap<>();
	}

	@Override
	public String authenticate(String token) throws AuthenticationException {
		if(!userTokens.containsKey(token)) {
			throw AuthenticationException.TOKENNOTFOUND;
		}
		UserToken userToken = userTokens.get(token);
		if(userToken.getTokenInstance().hasExpired()) {
			this.userTokens.remove(token);
			throw AuthenticationException.TOKENEXPIRED;
		}
		return userTokens.get(token).getUsername();
	}

	@Override
	public TokenInstance generateToken(String username, int durationSeconds) throws AuthenticationException {
		if(userTokens.entrySet().parallelStream().filter(e -> e.getValue().username.contentEquals(username)).findAny().isPresent()) {
			throw AuthenticationException.ACTIVETOKENEXISTS;
		}
		TokenInstance token = TokenInstance.generate(durationSeconds);
		this.userTokens.put(token.getToken(), new UserToken(token, username));
		return token;
	}

	@Override
	public TokenInstance refreshToken(String token, int durationSeconds) throws AuthenticationException {
		if(!userTokens.containsKey(token)) {
			throw AuthenticationException.TOKENNOTFOUND;
		}
		UserToken userToken = this.userTokens.get(token);
		TokenInstance tokenInstance = userToken.getTokenInstance();
		tokenInstance.refresh(durationSeconds);
		userToken.setTokenInstance(tokenInstance);
		this.userTokens.put(token, userToken);
		return tokenInstance;
	}
	
	private class UserToken {
		private TokenInstance tokenInstance;
		private String username;
		
		public UserToken(TokenInstance tokenInstance, String username) {
			this.tokenInstance = tokenInstance;
			this.username = username;
		}

		public TokenInstance getTokenInstance() {
			return tokenInstance;
		}

		public void setTokenInstance(TokenInstance tokenInstance) {
			this.tokenInstance = tokenInstance;
		}

		public String getUsername() {
			return username;
		}

		@SuppressWarnings("unused")
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
	
}
