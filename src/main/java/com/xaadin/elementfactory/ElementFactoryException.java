package com.xaadin.elementfactory;


import com.xaadin.XaadinException;

public class ElementFactoryException extends XaadinException {

	public ElementFactoryException(String message) {
		super(message);
	}

    public ElementFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementFactoryException(Throwable cause) {
        super(cause);
    }

}
