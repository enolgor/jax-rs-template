package es.enolgor.app.auth;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import es.enolgor.app.datasource.DataSource;
import es.enolgor.configuration.Configuration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTAuthenticationProvider implements TokenAuthenticationProvider {

	private final JwtBuilder jwtBuilder;
	private final JwtParser jwtParser;
	private final long accessTokenDuration;
	private final long refreshTokenDuration;
	private final DataSource dataSource;
	
	private static final String ISSUER_ACCESS = "access";
	private static final String ISSUER_REFRESH = "refresh";
	
	public JWTAuthenticationProvider(Configuration config, DataSource dataSource) {
		byte [] keyBytes = Base64.getDecoder().decode(config.getSecurityConfiguration().getJWTSecret());
		Key key = Keys.hmacShaKeyFor(keyBytes);
		this.jwtBuilder = Jwts.builder().signWith(key);
		this.jwtParser = Jwts.parser().setSigningKey(key);
		this.accessTokenDuration = ((long) config.getSecurityConfiguration().getAccessTokenDurationSeconds()) * 1000L;
		this.refreshTokenDuration = ((long) config.getSecurityConfiguration().getRefreshTokenDurationDays()) * 24L * 60L * 60L * 1000L;
		this.dataSource = dataSource;
	}
	
	@Override
	public String authenticateAccessToken(String token) throws AuthenticationException {
		try {
			Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
			String user = claims.getSubject();
			Date date = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			String issuer = claims.getIssuer();
			if(!issuer.contentEquals(ISSUER_ACCESS)) {
				throw AuthenticationException.INCORRECTTOKEN;
			}
			if(now.after(date)) {
				throw AuthenticationException.TOKENEXPIRED;
			}
			// String tokenId = claims.getId();
			return user;
		}catch(JwtException  e) {
			throw AuthenticationException.UNTRUSTEDTOKEN;
		}
	}

	@Override
	public String authenticateRefreshToken(String token) throws AuthenticationException {
		try {
			Claims claims = this.jwtParser.parseClaimsJws(token).getBody();
			String user = claims.getSubject();
			Date date = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			String issuer = claims.getIssuer();
			if(!issuer.contentEquals(ISSUER_REFRESH)) {
				throw AuthenticationException.INCORRECTTOKEN;
			}
			if(now.after(date)) {
				throw AuthenticationException.TOKENEXPIRED;
			}
			if(!this.dataSource.getUserTokenManager().tokenExists(user, token)) {
				throw AuthenticationException.TOKENNOTFOUND;
			}
			// String tokenId = claims.getId();
			return user;
		}catch(JwtException  e) {
			throw AuthenticationException.UNTRUSTEDTOKEN;
		}
	}

	@Override
	public String generateAccessToken(String username) {
		return jwtBuilder
			.setSubject(username)
			.setExpiration(new Date(System.currentTimeMillis() + this.accessTokenDuration))
			.setId(UUID.randomUUID().toString())
			.setIssuer(ISSUER_ACCESS)
			.compact();
	}

	@Override
	public String generateRefreshToken(String username) {
		return jwtBuilder
			.setSubject(username)
			.setExpiration(new Date(System.currentTimeMillis() + this.refreshTokenDuration))
			.setId(UUID.randomUUID().toString())
			.setIssuer(ISSUER_REFRESH)
			.compact();
	}
	
}
