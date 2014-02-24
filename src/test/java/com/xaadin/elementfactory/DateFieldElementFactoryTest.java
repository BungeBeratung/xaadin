package com.xaadin.elementfactory;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.xaadin.VisualTreeNode;
import com.xaadin.VisualTreeNodeImpl;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;


public class DateFieldElementFactoryTest {

	DateFieldElementFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new DateFieldElementFactory();
	}

	@Test
	public void testIsClassSupported() throws Exception {
		assertThat(factory.isClassSupportedForElementFactory(DateField.class.getName()));
		assertThat(factory.isClassSupportedForElementFactory(InlineDateField.class.getName()));
	}

	@Test
	public void testAddComponentToParentDateField() throws Exception {
		testAddComponentToParentByClass(DateField.class);
	}

	@Test
	public void testAddComponentToParentInlineDateField() throws Exception {
		testAddComponentToParentByClass(InlineDateField.class);
	}

	private void testAddComponentToParentByClass(Class dateFieldClass) throws Exception {
		HorizontalLayout layout = new HorizontalLayout();
		VisualTreeNode layoutTreeNode = new VisualTreeNodeImpl(layout);

		for (Resolution resolution : Resolution.values()) {
			DateField dateField = (DateField) dateFieldClass.newInstance();
			VisualTreeNodeImpl dateFieldTreeNode = new VisualTreeNodeImpl(dateField);
			dateFieldTreeNode.setAdditionalParameter("resolution", resolution.toString());
			factory.addComponentToParent(layoutTreeNode, dateFieldTreeNode);
			assertThat(dateField.getResolution()).isEqualTo(resolution);
		}
	}
}
