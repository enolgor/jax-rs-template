package es.enolgor.app.auth;

@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	private AuthenticationExceptionReason reason;
	
	private AuthenticationException(AuthenticationExceptionReason reason) {
		this.reason = reason;
	}
	
	public AuthenticationExceptionReason getReason() {
		return this.reason;
	}
	
	@Override
	public String getMessage() {
		return String.format("Authentication Exception: code = %d, reason = %s", this.reason.getCode(), this.reason.getMessage());
	}
	
	public static final class AuthenticationExceptionReason {
		private int code;
		private String message;
		
		public AuthenticationExceptionReason() {
			
		}
		
		public AuthenticationExceptionReason(int code, String message) {
			this.code = code;
			this.message = message;
		}
		
		public int getCode() {
			return code;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setCode(int code) {
			this.code = code;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	public static final AuthenticationException UNDEFINED = new AuthenticationException(new AuthenticationExceptionReason(1, "Authentication header is not present"));
	public static final AuthenticationException UNSUPPORTED = new AuthenticationException(new AuthenticationExceptionReason(2, "Unsupported authentication scheme"));
	public static final AuthenticationException MALFORMED = new AuthenticationException(new AuthenticationExceptionReason(3, "Malformed authentication header"));
	public static final AuthenticationException INCORRECTUSERPASS = new AuthenticationException(new AuthenticationExceptionReason(4, "Incorrect username or password"));
	public static final AuthenticationException TOKENNOTFOUND = new AuthenticationException(new AuthenticationExceptionReason(5, "Specified token not found"));
	public static final AuthenticationException TOKENEXPIRED = new AuthenticationException(new AuthenticationExceptionReason(6, "Specified token has expired"));
	public static final AuthenticationException UNTRUSTEDTOKEN = new AuthenticationException(new AuthenticationExceptionReason(7, "Specified token is not trusted or can't be verified"));
	public static final AuthenticationException INCORRECTTOKEN = new AuthenticationException(new AuthenticationExceptionReason(8, "Incorrect token"));
}
