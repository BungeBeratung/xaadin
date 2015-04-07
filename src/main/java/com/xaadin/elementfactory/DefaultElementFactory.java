package com.xaadin.elementfactory;


import com.xaadin.VisualTreeNode;

public class DefaultElementFactory extends AbstractDefaultLayoutElementFactory {

	private String packageName;

	public DefaultElementFactory(String packageName) {
		this.packageName = packageName;
	}

	public boolean isClassSupportedForElementFactory(String classname) {
		if (classname.startsWith(packageName)) {
			try {
				Class.forName(classname);
				return true;
			} catch (ClassNotFoundException ignored) {
			}
		}

		return false;
	}
}
