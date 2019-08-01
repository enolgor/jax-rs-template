package es.enolgor.app.api.auth;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.auth.AuthenticationException;

public abstract class AuthRequestFilter implements ContainerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthRequestFilter.class);
	
	private final AuthenticationScheme authenticationScheme;
	
	public AuthRequestFilter(AuthenticationScheme authenticationScheme) {
		this.authenticationScheme = authenticationScheme;
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			AuthenticationHeader authHeader = AuthenticationHeader.getFromContext(requestContext);
			if(authHeader.getAuthenticationScheme() != this.authenticationScheme) {
				throw AuthenticationException.UNSUPPORTED;
			}
			final String username = authenticate(authHeader);
			
			logger.info(String.format("%s authenticated successfully", username));
			
			final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
			
			requestContext.setSecurityContext(new SecurityContext() {

		        @Override
		        public Principal getUserPrincipal() {
		            return () -> username;
		        }

			    @Override
			    public boolean isUserInRole(String role) {
			        return true;
			    }

			    @Override
			    public boolean isSecure() {
			        return currentSecurityContext.isSecure();
			    }

			    @Override
			    public String getAuthenticationScheme() {
			        return authenticationScheme.toString();
			    }
			    
			});
			
		} catch (AuthenticationException e) {
			logger.error(e.getMessage());
			abort(requestContext, e);
		}
	}
	
	private void abort(ContainerRequestContext requestContext, AuthenticationException e) {
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, authenticationScheme.toString()).entity(e.getReason()).build());
	}
	
	public abstract String authenticate(AuthenticationHeader header) throws AuthenticationException;
}
