package es.enolgor.app.api.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.api.auth.AuthenticationHeader.BearerAuthenticationHeader;
import es.enolgor.app.auth.AuthenticationException;
import es.enolgor.app.auth.TokenAuthenticationProvider;

@Secured.Access
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AccessTokenAuthRequestFilter extends AuthRequestFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(AccessTokenAuthRequestFilter.class);
	
	@Inject TokenAuthenticationProvider tokenAuthenticator;

	public AccessTokenAuthRequestFilter() {
		super(AuthenticationScheme.BEARER);
	}

	@Override
	public String authenticate(AuthenticationHeader header) throws AuthenticationException {
		BearerAuthenticationHeader bearerAuthHeader = (BearerAuthenticationHeader) header;
		logger.info(String.format("Bearer Authentication Request: %s", bearerAuthHeader.getBearer()));
		return tokenAuthenticator.authenticateAccessToken(bearerAuthHeader.getBearer());
	}

}
