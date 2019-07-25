package es.enolgor.app.auth;

public interface BasicAuthenticationProvider {
	public boolean authenticate(String username, String password) throws AuthenticationException;
	
}
