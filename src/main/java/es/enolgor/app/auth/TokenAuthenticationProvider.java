package es.enolgor.app.auth;

public interface TokenAuthenticationProvider {
	public String authenticate(String token) throws AuthenticationException;
	public TokenInstance generateToken(String username, int durationSeconds) throws AuthenticationException;
	public TokenInstance refreshToken(String token, int durationSeconds) throws AuthenticationException;
}
