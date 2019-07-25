package es.enolgor.app.api.auth;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.app.api.ResponseUtils;
import es.enolgor.app.auth.AuthenticationException;
import es.enolgor.app.auth.TokenAuthenticationProvider;
import es.enolgor.app.auth.TokenInstance;
import es.enolgor.configuration.Configuration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Authorization API")
@Path("/authorization")
public class AuthAPI {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthAPI.class);
	
	@Context SecurityContext securityContext;
	@Inject TokenAuthenticationProvider tokenAuthenticator;
	@Inject Configuration configuration;
	
	@Secured.Basic
	@GET @Path("/token/generate")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Generate new authorization token", response = TokenInstance.class)
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response generateToken(){
		logger.info("Authentication success");
		TokenInstance tokenInstance;
		try {
			tokenInstance = tokenAuthenticator.generateToken(securityContext.getUserPrincipal().getName(), configuration.getSecurityConfiguration().getTokenDurationSeconds());
		} catch (AuthenticationException e) {
			return ResponseUtils.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
		return ResponseUtils.success(tokenInstance);
 	}
	
	@Secured.Basic
	@POST @Path("/token/refresh")
	@Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Refresh authorization token", response = TokenInstance.class)
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response refreshToken(@ApiParam(value = "Token", required = true) String token){
		TokenInstance tokenInstance;
		try {
			tokenInstance = tokenAuthenticator.refreshToken(token, configuration.getSecurityConfiguration().getTokenDurationSeconds());
		} catch (AuthenticationException e) {
			return ResponseUtils.error(e.getMessage(), Response.Status.UNAUTHORIZED.getStatusCode());
		}
		return ResponseUtils.success(tokenInstance);
 	}
	
}