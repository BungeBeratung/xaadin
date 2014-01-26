package com.xaadin.elementfactory;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;
import com.xaadin.VisualTreeNode;

public class AbstractSplitPanelElementFactory extends AbstractDefaultElementFactory {
	@Override
	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		AbstractSplitPanel panel = parent.getComponent();

		int childsAdded = Integer.parseInt(parent.getAdditionalParameter("firstChildAdded", "0"));

		switch (childsAdded) {
			case 0:
				panel.setFirstComponent((Component) child.getComponent());
				break;
			case 1:
				panel.setSecondComponent((Component) child.getComponent());
				break;
			default:
				throw new ElementFactoryException("Splitpanels can only have two child controls");
		}

		float spacerPosition = getFloatFromVisualTreeNode("AbstractSplitPanel.splitPosition", parent);
		if (Math.abs(spacerPosition) > 0.01f) {
			panel.setSplitPosition(spacerPosition * 100.f, Sizeable.Unit.PERCENTAGE);
		}

		childsAdded++;
		parent.setAdditionalParameter("firstChildAdded", Integer.toString(childsAdded));
	}

	@Override
	public void processEvents(VisualTreeNode child, Object eventHandlerTarget) throws ElementFactoryException {
	}

	@Override
	public boolean isClassSupportedForElementFactory(String classname) {
		return classname.equals(HorizontalSplitPanel.class.getName())
				|| classname.equals(VerticalSplitPanel.class.getName());
	}
}
