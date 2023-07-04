package com.xaadin.elementfactory;


import com.vaadin.ui.Panel;
import com.xaadin.VisualTreeNode;

public class PanelElementFactory extends AbstractDefaultLayoutElementFactory {

	@Override
	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		Panel panel = parent.getComponent();
		panel.setContent(child.getComponent());
	}

	public boolean isClassSupportedForElementFactory(String classname) {
		return Panel.class.getName().equals(classname);
	}
}
