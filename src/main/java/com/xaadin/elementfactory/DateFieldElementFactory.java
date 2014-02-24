package com.xaadin.elementfactory;

import com.google.common.base.Strings;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.xaadin.VisualTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFieldElementFactory extends AbstractDefaultLayoutElementFactory {

	private final static Logger LOG = LoggerFactory.getLogger(DateFieldElementFactory.class);

	@Override
	public void processEvents(VisualTreeNode child, Object eventHandlerTarget) throws ElementFactoryException {

	}

	@Override
	public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
		super.addComponentToParent(parent, child);

		DateField dateField = child.getComponent();
		String resolutionValue = child.getAdditionalParameter("resolution", "");
		if (!Strings.isNullOrEmpty(resolutionValue)) {
			Resolution resolution = Resolution.valueOf(resolutionValue);
			if (resolution != null) {
				dateField.setResolution(resolution);
			} else {
				LOG.warn("unknown value for resolution: " + resolutionValue);
			}
		}
	}

	@Override
	public boolean isClassSupportedForElementFactory(String classname) {
		return classname.equals(DateField.class.getName()) || classname.equals(InlineDateField.class.getName());
	}
}
