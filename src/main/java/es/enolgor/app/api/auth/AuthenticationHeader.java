package es.enolgor.app.api.auth;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import es.enolgor.app.auth.AuthenticationException;

public abstract class AuthenticationHeader {
	
	public static enum Scheme {
		
		BASIC("Basic"), BEARER("Bearer");
		
		private String schemeString;
		
		private Scheme(String schemeString) {
			this.schemeString = schemeString;
		}
		
		@Override
		public String toString() {
			return schemeString;
		}
		
		public static Scheme get(String authenticationHeader) throws AuthenticationException {
			if(authenticationHeader == null) throw AuthenticationException.UNDEFINED;
			for(Scheme scheme : Scheme.values()) {
				if(authenticationHeader.toLowerCase().startsWith(scheme.schemeString.toLowerCase() + " ")) return scheme;
			}
			throw AuthenticationException.UNSUPPORTED;
		}
		
	}
	
	private Scheme scheme;
	
	private AuthenticationHeader(Scheme scheme) {
		this.scheme = scheme;
	}
	
	public Scheme getScheme() {
		return this.scheme;
	}
	
	public static AuthenticationHeader getFromContext(ContainerRequestContext requestContext) throws AuthenticationException {
		String authenticationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		Scheme scheme = Scheme.get(authenticationHeader);
		String authValue = authenticationHeader.substring(authenticationHeader.indexOf(" ") + 1);
		switch(scheme) {
		case BASIC: return new BasicAuthenticationHeader(authValue);
		case BEARER: return new BearerAuthenticationHeader(authValue);
		default: return null; // NEVER SHOULD ARRIVE
		}
	}
	
	public static class BasicAuthenticationHeader extends AuthenticationHeader {

		private String username;
		private String password;
		
		private BasicAuthenticationHeader(String b64auth) throws AuthenticationException {
			super(Scheme.BASIC);
			this.username = null;
			this.password = null;
			String usernameAndPassword = null;
			try {
				byte[] decodedBytes = Base64.getDecoder().decode(b64auth);
				usernameAndPassword = new String(decodedBytes, "UTF-8");
			} catch (IOException e) {
				throw AuthenticationException.MALFORMED;
			}
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			this.username = tokenizer.nextToken();
			this.password = tokenizer.nextToken();
		}
		
		public String getUsername() {
			return username;
		}
		
		public String getPassword() {
			return password;
		}
		
	}
	
	public static class BearerAuthenticationHeader extends AuthenticationHeader {

		private String bearer;
		
		private BearerAuthenticationHeader(String bearer) throws AuthenticationException{
			super(Scheme.BEARER);
			this.bearer = bearer;
		}
		
		public String getBearer() {
			return bearer;
		}
		
	}
	
}
