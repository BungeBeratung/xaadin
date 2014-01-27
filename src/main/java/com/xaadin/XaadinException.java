package com.xaadin;

public class XaadinException extends Exception {

	public XaadinException(String message) {
		super(message);
	}

	public XaadinException(Throwable cause) {
		super(cause);
	}

    public XaadinException(String message, Throwable cause) {
        super(message, cause);
    }
}
