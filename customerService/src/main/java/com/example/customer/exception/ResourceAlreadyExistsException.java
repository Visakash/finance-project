package com.example.customer.exception;

public class ResourceAlreadyExistsException  extends RuntimeException {
	
	public ResourceAlreadyExistsException (String msg) {
		super(msg);
	}

}
