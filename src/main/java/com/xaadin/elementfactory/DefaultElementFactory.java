package com.xaadin.elementfactory;


public class DefaultElementFactory extends AbstractDefaultLayoutElementFactory {

	private String packageName;

	public DefaultElementFactory(String packageName) {
		this.packageName = packageName;
	}

	public boolean isClassSupportedForElementFactory(String classname) {
		if (classname.startsWith(packageName)) {
			try {
				Class<?> aClass = Class.forName(classname);
				return aClass != null;
			} catch (ClassNotFoundException ignored) {
			}
		}

		return false;
	}
}
