package com.xaadin;

import com.vaadin.ui.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualTreeNodeImpl implements VisualTreeNode {

	private List<VisualTreeNode> visualTreeNodeList = new ArrayList<>();

	private String id;
	private Object component;

	private Map<String, String> additionalAttributes = new HashMap<>();

	public VisualTreeNodeImpl(Object component) {
		this.component = component;
	}

	public void setAdditionalParameter(String name, String value) {
		additionalAttributes.put(name, value);
	}

	public String getAdditionalParameter(String name, String defaultValue) {
		if (additionalAttributes.containsKey(name)) {
			return additionalAttributes.get(name);
		} else {
			return defaultValue;
		}
	}

	public String getId() {
		tryToResolveId();
		return id;
	}

	private void tryToResolveId() {
		if (id == null) {
			if (component instanceof Component) {
				id = ((Component) component).getId();
			} else {
				id = "";
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent() {
		return (T) component;
	}

	public void addVisualTreeNode(VisualTreeNode node) {
		visualTreeNodeList.add(node);
	}

	private boolean equalsString(String s1, String s2) {
		return !((s1 == null) || (s2 == null)) && s1.equals(s2);
	}

	public VisualTreeNode findVisualTreeNodeById(String id) {
		for (VisualTreeNode node : visualTreeNodeList) {
			if (equalsString(node.getId(), id))
				return node;

			// search subnodes
			VisualTreeNode subNode = node.findVisualTreeNodeById(id);
			if (subNode != null)
				return subNode;
		}
		return null;
	}

	public VisualTreeNode findVisualTreeNodeByComponent(Component component) {
		for (VisualTreeNode node : visualTreeNodeList) {
			if (component == node.getComponent())
				return node;

			// search subnodes
			VisualTreeNode subNode = node.findVisualTreeNodeByComponent(component);
			if (subNode != null)
				return subNode;
		}
		return null;
	}

	public <T> T findComponentById(String id) {
		VisualTreeNode node = findVisualTreeNodeById(id);
		if (node != null)
			return node.getComponent();
		else
			return null;
	}
}
