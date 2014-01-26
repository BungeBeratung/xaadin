package com.xaadin.elementfactory;

import com.google.common.base.Strings;
import com.vaadin.ui.Button;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ButtonElementFactory extends AbstractDefaultLayoutElementFactory {

	final static Logger logger = LoggerFactory.getLogger(ButtonElementFactory.class);

	public void processEvents(final VisualTreeNode child, final Object eventHandlerTarget) throws ElementFactoryException {
		final String clickEventHandlerName = child.getAdditionalParameter("onClick", null);

		if (!Strings.isNullOrEmpty(clickEventHandlerName)) {
			final Method method = findMethodForName(eventHandlerTarget.getClass(), clickEventHandlerName, 2);
			Button button = child.getComponent();
			button.addClickListener(new Button.ClickListener() {
				public void buttonClick(Button.ClickEvent event) {
					try {
						invokeMethod(method, eventHandlerTarget, child, event);
					} catch (Exception e) {
						logger.error("could not invoke button function", e);
						throw new RuntimeException(e);
					}
				}
			});
		}
	}

	public boolean isClassSupportedForElementFactory(String classname) {
		return classname.equals(Button.class.getName());
	}
}
