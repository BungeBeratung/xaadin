package com.xaadin;

import com.vaadin.ui.Component;

public interface VisualTreeNode {

	/**
	 * gets the unique id of this VisualTreeNode, its the same as the vaadin id of the control
	 *
	 * @return the id of the component or an empty string, if the component has no id
	 */
	String getId();

	/**
	 * gets the component associated with this VisualTreeNode
	 *
	 * @param <T> the exspected component type
	 * @return the component associated with this VisualTreeNode
	 */
	<T> T getComponent();

	/**
	 * sets an additional parameter for this node. This can either be an additional parameter for layouting or
	 * a custom user parameter for usage in the code during callbacks or other stuff
	 *
	 * @param name  the name of the parameter
	 * @param value the value of the parameter
	 */
	void setAdditionalParameter(String name, String value);

	/**
	 * gets an additional parameter from this visualtree node. This can either be an additional parameter for layouting
	 * or a custom user parameter for usage in the code during callbacks or other stuff
	 *
	 * @param name         the name of the parameter
	 * @param defaultValue a default value, which will be returned, if the parameter couldn't be found in this
	 *                     VisualTreeNode
	 * @return the value of the additional parameter or the defaultValue, if a parameter with this name couln't be found
	 */
	String getAdditionalParameter(String name, String defaultValue);

	/**
	 * adds a VisualTreeNode as a child to this Node
	 *
	 * @param node the new child VisualTreeNode
	 */
	void addVisualTreeNode(VisualTreeNode node);

	/**
	 * finds a VisualTreeNode in this node and also searches its children for the given id
	 *
	 * @param id the id of the node to search
	 * @return the VisualTreeNode with the given id or null, if the VisualTreeNode couldn't be found within this node
	 * or its children
	 */
	VisualTreeNode findVisualTreeNodeById(String id);

	/**
	 * finds a VisualTreeNode in this node and also searches its children for the given component
	 *
	 * @param component the component of the VisualTreeNode to find
	 * @return the VisualTreeNode with the given component or null, if the VisualTreeNode couldn't be found within this node
	 * or its children
	 */
	VisualTreeNode findVisualTreeNodeByComponent(Component component);

	/**
	 * finds the vaading component with the given id and also searches its children for the given component
	 *
	 * @param id  the id of the component to search
	 * @param <T> the type of the target component
	 * @return the component with the given id or null, if the component couldn't be found within this VisualTreeNode
	 * or its children
	 */
	<T> T findComponentById(String id);
}
