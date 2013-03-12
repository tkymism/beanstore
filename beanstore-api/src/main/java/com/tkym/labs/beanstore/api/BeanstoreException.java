package com.tkym.labs.beanstore.api;

@SuppressWarnings("serial")
public class BeanstoreException extends RuntimeException{
	public BeanstoreException() {
		super();
	}
	public BeanstoreException(String message, Throwable cause) {
		super(message, cause);
	}
	public BeanstoreException(String message) {
		super(message);
	}
	public BeanstoreException(Throwable cause) {
		super(cause);
	}
}
