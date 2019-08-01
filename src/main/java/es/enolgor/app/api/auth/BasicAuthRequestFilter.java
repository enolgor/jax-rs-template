package es.enolgor.app.api.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.api.auth.AuthenticationHeader.BasicAuthenticationHeader;
import es.enolgor.app.auth.AuthenticationException;
import es.enolgor.app.auth.BasicAuthenticationProvider;

@Secured.Basic
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthRequestFilter extends AuthRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(BasicAuthRequestFilter.class);
	
	@Inject BasicAuthenticationProvider basicAuthenticator;

	public BasicAuthRequestFilter() {
		super(AuthenticationScheme.BASIC);
	}

	@Override
	public String authenticate(AuthenticationHeader header) throws AuthenticationException {
		BasicAuthenticationHeader basicAuthHeader = (BasicAuthenticationHeader) header;
		logger.info(String.format("Basic Authentication Request: %s, %s", basicAuthHeader.getUsername(), basicAuthHeader.getPassword()));
		if (!basicAuthenticator.authenticate(basicAuthHeader.getUsername(), basicAuthHeader.getPassword())) {
			throw AuthenticationException.INCORRECTUSERPASS;
		};
		return basicAuthHeader.getUsername();
	}

}
