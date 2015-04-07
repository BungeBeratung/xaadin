package com.xaadin.elementfactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractDefaultElementFactory implements ElementFactory {

	private final static Logger LOG = LoggerFactory.getLogger(AbstractDefaultElementFactory.class);

	public Object createClass(Object parentObject, String classname) throws ElementFactoryException {
		try {
			return Class.forName(classname).newInstance();
		} catch (Exception e) {
			throw new ElementFactoryException(e);
		}
	}

	public void setItemStyles(Object component, String[] styles) {
		if (component instanceof Component) {
			for (String oldStyleName : ((Component) component).getStyleName().split(" ")) {
				((Component) component).removeStyleName(oldStyleName);
			}

			for (String style : styles) {
				((Component) component).addStyleName(style);
			}
		}
	}

	protected Method findMethodForName(Class clazz, String methodName, int parameterCount) throws ElementFactoryException {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)
					&& (method.getParameterTypes().length == parameterCount)) {
				return method;
			}
		}

		// if no public method was found, try to find a private one
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName)
					&& (method.getParameterTypes().length == parameterCount)) {
				return method;
			}
		}

		throw new ElementFactoryException("could not find " + clazz.getName() + "." + methodName);
	}

	protected void invokeMethod(Method method, Object eventHandler, Object... parameters) throws InvocationTargetException, IllegalAccessException {
		LOG.trace("Invoking {}", method);
		boolean accessible = method.isAccessible();
		method.setAccessible(true);
		method.invoke(eventHandler, parameters);
		method.setAccessible(accessible);
	}

	public void setProperty(Object target, String property, String value) throws ElementFactoryException {
		try {
			handleNormalProperty(target, property, value);
		} catch (Exception e) {
			throw new ElementFactoryException(e);
		}
	}

	protected Alignment parseAlignment(String alignment) {
		if (alignment == null || alignment.isEmpty()) {
			return Alignment.MIDDLE_LEFT;
		}

		switch (alignment) {
			case "TOP_LEFT":
				return Alignment.TOP_LEFT;
			case "TOP_CENTER":
				return Alignment.TOP_CENTER;
			case "TOP_RIGHT":
				return Alignment.TOP_RIGHT;
			case "MIDDLE_LEFT":
				return Alignment.MIDDLE_LEFT;
			case "MIDDLE_CENTER":
				return Alignment.MIDDLE_CENTER;
			case "MIDDLE_RIGHT":
				return Alignment.MIDDLE_RIGHT;
			case "BOTTOM_LEFT":
				return Alignment.BOTTOM_LEFT;
			case "BOTTOM_CENTER":
				return Alignment.BOTTOM_CENTER;
			case "BOTTOM_RIGHT":
				return Alignment.BOTTOM_RIGHT;
			default:
				LOG.warn("unknown alignment for GridLayout: {}", alignment);
				return Alignment.MIDDLE_LEFT;
		}
	}

	private void handleNormalProperty(Object target, String property, String value) throws ElementFactoryException, InvocationTargetException, IllegalAccessException {
		Class clazz = target.getClass();
		String setterName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);

		Method method = findMethodForName(clazz, setterName, 1);
		if (method == null) {
			throw new ElementFactoryException("could not find setter for attribute " + property + " on target " + target.getClass().getName());
		}
		if (method.getParameterTypes()[0].equals(int.class) || method.getParameterTypes()[0].equals(Integer.class)) {
			invokeMethod(method, target, Integer.parseInt(value));
		} else if (method.getParameterTypes()[0].equals(double.class) || method.getParameterTypes()[0].equals(Double.class)) {
			invokeMethod(method, target, Double.parseDouble(value));
		} else if (method.getParameterTypes()[0].equals(boolean.class) || method.getParameterTypes()[0].equals(Boolean.class)) {
			invokeMethod(method, target, Boolean.parseBoolean(value));
        } else if (method.getParameterTypes()[0].isEnum()) {
            Object[] enumValues = method.getParameterTypes()[0].getEnumConstants();
            for (Object enumValue : enumValues) {
                if (enumValue.toString().equals(value)) {
                    invokeMethod(method, target, enumValue);
                    break;
                }
            }
        } else {
            invokeMethod(method, target, value);
		}
	}

	protected float getFloatFromVisualTreeNode(String parameterName, VisualTreeNode node) {
		try {
			return Float.parseFloat(node.getAdditionalParameter(parameterName, "0.0"));
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	@Override
	public void processEvents(VisualTreeNode child, Object eventHandlerTarget) throws ElementFactoryException {
		// Override this method if you like to handle events.
	}
}
