package com.xaadin.elementfactory;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitPanelElementFactory extends AbstractDefaultElementFactory {

    final static Logger logger = LoggerFactory.getLogger(SplitPanelElementFactory.class);

    @Override
	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		AbstractSplitPanel panel = parent.getComponent();

        int componentCount = panel.getComponentCount();
        switch (componentCount) {
            case 0:
				panel.setFirstComponent((Component) child.getComponent());
				break;
			case 1:
				panel.setSecondComponent((Component) child.getComponent());
				break;
			default:
				throw new ElementFactoryException("Splitpanels can only have two child controls");
		}

        float spacerPosition;
        if (!parent.getAdditionalParameter("AbstractSplitPanel.splitPosition", "").isEmpty()) {
            spacerPosition = getFloatFromVisualTreeNode("AbstractSplitPanel.splitPosition", parent);
            logger.warn("Deprecated attribute 'AbstractSplitPanel.splitPosition' used for "
                    + parent.getComponent().getClass().getName()
                    + ". Attribute will be removed in further versions of xaadin. Please only use the attribute 'splitPosition'.");
        } else {
            spacerPosition = getFloatFromVisualTreeNode("splitPosition", parent);
        }

        if (Math.abs(spacerPosition) > 0.01f) {
			panel.setSplitPosition(spacerPosition * 100.f, Sizeable.Unit.PERCENTAGE);
		}
	}

	@Override
	public boolean isClassSupportedForElementFactory(String classname) {
		return classname.equals(HorizontalSplitPanel.class.getName())
				|| classname.equals(VerticalSplitPanel.class.getName());
	}
}
