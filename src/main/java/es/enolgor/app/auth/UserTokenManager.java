package es.enolgor.app.auth;

public interface UserTokenManager {
	public boolean tokenExists(String username, String token);
	public void revokeToken(String username, String token);
	public void revokeAllTokens(String username);
	public void addToken(String username, String token);
}
