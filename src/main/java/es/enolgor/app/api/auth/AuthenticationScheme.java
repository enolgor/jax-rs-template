package es.enolgor.app.api.auth;

import es.enolgor.app.auth.AuthenticationException;

public enum AuthenticationScheme {
	
	BASIC("Basic"), BEARER("Bearer");
	
	private String schemeString;
	
	private AuthenticationScheme(String schemeString) {
		this.schemeString = schemeString;
	}
	
	@Override
	public String toString() {
		return schemeString;
	}
	
	public static AuthenticationScheme getFromHeader(String authenticationHeader) throws AuthenticationException {
		if(authenticationHeader == null) throw AuthenticationException.UNDEFINED;
		for(AuthenticationScheme scheme : AuthenticationScheme.values()) {
			if(authenticationHeader.toLowerCase().startsWith(scheme.schemeString.toLowerCase() + " ")) return scheme;
		}
		throw AuthenticationException.UNSUPPORTED;
	}
	
}