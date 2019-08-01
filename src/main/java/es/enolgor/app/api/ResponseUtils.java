package es.enolgor.app.api;

import javax.ws.rs.core.Response;

public class ResponseUtils {
	
	public static Response success() {
		return ResponseUtils.success(null);
	}
	
	public static Response success(Object entity) {
		return ResponseUtils.success(entity, 200);
	}
	
	public static Response success(Object entity, int status) {
		return Response.status(status).entity(entity).build();
	}
	
	public static Response error() {
		return ResponseUtils.success(null);
	}
	
	public static Response error(String message) {
		return ResponseUtils.success(message, 500);
	}
	
	public static Response error(Object message, int status) {
		return Response.status(status).entity(message).build();
	}
	
}
