package com.xaadin.elementfactory;

import com.vaadin.ui.*;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractOrderedLayoutElementFactory extends AbstractDefaultElementFactory {

    final static Logger logger = LoggerFactory.getLogger(AbstractOrderedLayoutElementFactory.class);

    public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) {
        if (!(parent.getComponent() instanceof AbstractOrderedLayout)) {
            throw new IllegalArgumentException("parent is not a descendent of com.vaadin.ui.AbstractOrderedLayout");
        }

        AbstractOrderedLayout layout = parent.getComponent();
        layout.addComponent((Component) child.getComponent());

        float expandRatio;
        if (!child.getAdditionalParameter("AbstractOrderedLayout.expandRatio", "").isEmpty()) {
            expandRatio = getFloatFromVisualTreeNode("AbstractOrderedLayout.expandRatio", child);
            logger.warn("Deprecated attribute 'AbstractOrderedLayout.expandRatio' used for "
                    + child.getComponent().getClass().getName()
                    + ". Attribute will be removed in further versions of xaadin. Please only use the attribute 'expandRatio'.");
        } else {
            expandRatio = getFloatFromVisualTreeNode("expandRatio", child);
        }
        layout.setExpandRatio((Component) child.getComponent(), expandRatio);

        Alignment alignment = parseAlignment(child.getAdditionalParameter("alignment", "TOP_LEFT"));
        layout.setComponentAlignment((Component) child.getComponent(), alignment);
    }

    public void processEvents(VisualTreeNode child, Object eventHandlerTarget) {

    }

    public boolean isClassSupportedForElementFactory(String classname) {
        return classname.equals(VerticalLayout.class.getName()) || classname.equals(HorizontalLayout.class.getName());
    }
}
