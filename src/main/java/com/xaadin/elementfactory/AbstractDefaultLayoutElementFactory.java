package com.xaadin.elementfactory;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.xaadin.VisualTreeNode;

public abstract class AbstractDefaultLayoutElementFactory extends AbstractDefaultElementFactory {

	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		if (parent.getComponent() instanceof AbstractComponentContainer) {
			Component childComponent = child.getComponent();
			AbstractComponentContainer parentContainer = parent.getComponent();
			parentContainer.addComponent(childComponent);

			if (parentContainer instanceof AbstractOrderedLayout) {
                Alignment alignment = parseAlignment(child.getAdditionalParameter("alignment", "MIDDLE_LEFT"));
				((AbstractOrderedLayout) parentContainer).setComponentAlignment(childComponent, alignment);
            }
		}
	}

}
