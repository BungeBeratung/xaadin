package com.xaadin.elementfactory;

import com.vaadin.ui.*;
import com.xaadin.VisualTreeNode;

public class AbstractOrderedLayoutElementFactory extends AbstractDefaultElementFactory {

	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) {
		if (!(parent.getComponent() instanceof AbstractOrderedLayout)) {
			throw new IllegalArgumentException("parent is not a descendent of com.vaadin.ui.AbstractOrderedLayout");
		}

		AbstractOrderedLayout layout = parent.getComponent();
		layout.addComponent((Component) child.getComponent());

		float expandRatio = getFloatFromVisualTreeNode("AbstractOrderedLayout.expandRatio", child);
		layout.setExpandRatio((Component) child.getComponent(), expandRatio);

		Alignment alignment = parseAlignment(child.getAdditionalParameter("alignment", "TOP_LEFT"));
		layout.setComponentAlignment((Component) child.getComponent(), alignment);

	}

	public void processEvents(VisualTreeNode child, Object eventHandlerTarget) {

	}

	public boolean isClassSupportedForElementFactory(String classname) {
		//return classname.equals(GridLayout.class.getName());
		return classname.equals(VerticalLayout.class.getName()) || classname.equals(HorizontalLayout.class.getName());
	}
}
