package com.xaadin.elementfactory;


import com.xaadin.VisualTreeNode;

public interface ElementFactory {

	Object createClass(Object parentObject, String classname) throws ElementFactoryException;

	void setProperty(Object target, String property, String value) throws ElementFactoryException;

	void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException;

	void processEvents(VisualTreeNode child, Object eventHandlerTarget) throws ElementFactoryException;

	boolean isClassSupportedForElementFactory(String classname);

	void setItemStyles(Object component, String[] styles);
}
