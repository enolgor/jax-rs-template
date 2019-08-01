package es.enolgor.app.api.auth;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.enolgor.app.api.ResponseUtils;
import es.enolgor.app.auth.TokenAuthenticationProvider;
import es.enolgor.app.datasource.DataSource;
import es.enolgor.configuration.Configuration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Api("Authorization API")
@Path("/authorization")
public class AuthAPI {
	
	@Context SecurityContext securityContext;
	@Inject TokenAuthenticationProvider tokenAuthenticator;
	@Inject Configuration configuration;
	@Inject DataSource dataSource;
	
	@Secured.Basic
	@GET @Path("/token/generate")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Generate new authorization access and refresh token", response = GeneratedToken.class, authorizations = @Authorization("basic"))
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response generateToken(){
		String username = securityContext.getUserPrincipal().getName();
		String refreshToken = tokenAuthenticator.generateRefreshToken(username);
		String accessToken = tokenAuthenticator.generateAccessToken(username);
		dataSource.getUserTokenManager().addToken(username, refreshToken);
		GeneratedToken tokenInstance = new GeneratedToken(accessToken, refreshToken);
		return ResponseUtils.success(tokenInstance);
 	}
	
	@Secured.Refresh
	@GET @Path("/token/refresh")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Generate new access token given the refresh token", response = GeneratedToken.class, authorizations = @Authorization("refresh"))
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response refreshToken(){
		String username = securityContext.getUserPrincipal().getName();
		String accessToken = tokenAuthenticator.generateAccessToken(username);
		return ResponseUtils.success(new GeneratedToken(accessToken, null));
		
 	}
	
	@Secured.Access
	@GET @Path("/token/revoke/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Revoke specified refresh token", authorizations = @Authorization("access"))
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response revokeToken(@ApiParam(value = "Refresh Token", required = true) @PathParam("token") String token){
		String username = securityContext.getUserPrincipal().getName();
		dataSource.getUserTokenManager().revokeToken(username, token);
		return ResponseUtils.success();
 	}
	
	@Secured.Access
	@GET @Path("/token/revokeAll")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Revoke all refresh tokens", authorizations = @Authorization("access"))
	@ApiResponses({
		@ApiResponse(code = 401, message = "Unauthorized")
	})
	public Response revokeAll(){
		String username = securityContext.getUserPrincipal().getName();
		dataSource.getUserTokenManager().revokeAllTokens(username);
		return ResponseUtils.success();
 	}
	
	public static class GeneratedToken {
		@JsonInclude(Include.NON_NULL) private String accessToken;
		@JsonInclude(Include.NON_NULL) private String refreshToken;
		
		public GeneratedToken(String accessToken, String refreshToken) {
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
		
		public String getAccessToken() {
			return accessToken;
		}
		
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		
		public String getRefreshToken() {
			return refreshToken;
		}
		
		public void setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
		}
	}
	
}