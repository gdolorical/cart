package com.amaysim.exception;

public class InvalidPromoCodeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidPromoCodeException() { super(); }
	public InvalidPromoCodeException(String message) { super(message); }
	public InvalidPromoCodeException(String message, Throwable cause) { super(message, cause); }
	public InvalidPromoCodeException(Throwable cause) { super(cause); }

}
