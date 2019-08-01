package es.enolgor.app.auth;

public interface TokenAuthenticationProvider {
	public String authenticateAccessToken(String token) throws AuthenticationException;
	public String authenticateRefreshToken(String token) throws AuthenticationException;
	public String generateAccessToken(String username);
	public String generateRefreshToken(String username);
}
