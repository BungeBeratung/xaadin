package com.xaadin.elementfactory;

import com.google.common.base.Strings;
import com.vaadin.ui.MenuBar;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class MenuItemElementFactory extends AbstractDefaultElementFactory {

	final static Logger logger = LoggerFactory.getLogger(MenuItemElementFactory.class);

	@Override
	public Object createClass(Object parentObject, String classname) throws ElementFactoryException {
		if (parentObject instanceof MenuBar)
			return ((MenuBar) parentObject).addItem("dummy", null);
		else if (parentObject instanceof MenuBar.MenuItem)
			return ((MenuBar.MenuItem) parentObject).addItem("dummy", null);

		throw new ElementFactoryException("Menubar.Menuitem parent must be of type MenuBar");
	}

	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		// do nothing menuitems are automatically added
	}

	public void processEvents(final VisualTreeNode child, final Object eventHandlerTarget) throws ElementFactoryException {
		final String clickEventHandlerName = child.getAdditionalParameter("onClick", null);

		if (!Strings.isNullOrEmpty(clickEventHandlerName)) {
			final Method method = findMethodForName(eventHandlerTarget.getClass(), clickEventHandlerName, 2);

			MenuBar.MenuItem menuItem = child.getComponent();
			menuItem.setCommand(new MenuBar.Command() {
				public void menuSelected(MenuBar.MenuItem selectedItem) {
					try {
						invokeMethod(method, eventHandlerTarget, child, selectedItem);
					} catch (Exception e) {
						logger.error("could not invoke menu function", e);
					}
				}
			});
		}
	}

	public boolean isClassSupportedForElementFactory(String classname) {
		return classname.equals(MenuBar.MenuItem.class.getName().replace("$", "."));
	}
}
