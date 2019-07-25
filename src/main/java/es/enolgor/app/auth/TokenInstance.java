package es.enolgor.app.auth;

import java.util.UUID;

public class TokenInstance {
	
	private String token;
	private long expiration;
	
	public String getToken() {
		return token;
	}
	
	public long getExpiration() {
		return expiration;
	}
	
	public static TokenInstance generate(int durationSeconds) {
		UUID uuid = UUID.randomUUID();
		TokenInstance token = new TokenInstance();
		token.token = uuid.toString();
		token.expiration = System.currentTimeMillis() + durationSeconds * 1000L;
		return token;
	}
	
	public boolean hasExpired() {
		return System.currentTimeMillis() > this.expiration;
	}
	
	public void refresh(int durationSeconds) {
		this.expiration = System.currentTimeMillis() + durationSeconds * 1000L;
	}
	
}
