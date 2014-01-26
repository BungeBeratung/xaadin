package com.xaadin.elementfactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.xaadin.VisualTreeNode;
import com.xaadin.VisualTreeNodeImpl;
import org.fest.assertions.data.Offset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractDefaultElementFactoryTest {

	private AbstractDefaultElementFactory factory;

	@Before
	public void setUp() {
		factory = Mockito.mock(AbstractDefaultElementFactory.class, Mockito.CALLS_REAL_METHODS);
	}

	@Test
	public void testCreateClass() throws Exception {
		Object o = factory.createClass(null, Button.class.getName());
		assertThat(o).isNotNull();
		assertThat(o).isOfAnyClassIn(Button.class);
	}

	@Test(expected = ElementFactoryException.class)
	public void testCreateClassNotFound() throws Exception {
		factory.createClass(null, Button.class.getSimpleName());

	}

	@Test
	public void testSetItemStyles() throws Exception {
		Button btn = new Button();
		factory.setItemStyles(btn, new String[]{"a", "b", "c"});
		assertThat(btn.getStyleName()).isEqualTo("a b c");

		factory.setItemStyles(btn, new String[]{"c", "b", "d"});
		assertThat(btn.getStyleName()).isEqualTo("c b d");
	}

	@Test
	public void testFindMethodForName() throws Exception {
		Method m = factory.findMethodForName(Button.class, "setTabIndex", 1);
		assertThat(m.getName()).isEqualTo("setTabIndex");
		assertThat(m.getParameterTypes()).hasSize(1);
	}

	@Test
	public void testSetProperty() throws Exception {
		Button btn = new Button();
		factory.setProperty(btn, "width", "123");
		assertThat(btn.getWidth()).isEqualTo(123f, Offset.offset(0.01f));
	}

	@Test
	public void testParseAlignment() throws Exception {
		Alignment[] alignments = new Alignment[]{
			Alignment.TOP_LEFT, Alignment.TOP_CENTER, Alignment.TOP_RIGHT,
			Alignment.MIDDLE_LEFT, Alignment.MIDDLE_CENTER, Alignment.MIDDLE_RIGHT,
			Alignment.BOTTOM_LEFT, Alignment.BOTTOM_CENTER, Alignment.BOTTOM_RIGHT
		};
		String[] alignmentStrings = new String[]{
			"TOP_LEFT", "TOP_CENTER", "TOP_RIGHT",
			"MIDDLE_LEFT", "MIDDLE_CENTER", "MIDDLE_RIGHT",
			"BOTTOM_LEFT", "BOTTOM_CENTER", "BOTTOM_RIGHT"
		};

		for (int i = 0; i < alignments.length; i++) {
			Alignment alignment = factory.parseAlignment(alignmentStrings[i]);
			assertThat(alignment).isEqualTo(alignments[i]);
		}
	}

	@Test
	public void testGetFloatFromVisualTreeNode() throws Exception {
		VisualTreeNode node = new VisualTreeNodeImpl(null);
		node.setAdditionalParameter("test", "0.12345");
		float f = factory.getFloatFromVisualTreeNode("test", node);
		assertThat(f).isEqualTo(0.12345f, Offset.offset(0.00001f));
	}

	@Test
	public void testGetFloatFromVisualTreeNodeInvalidNumber() throws Exception {
		VisualTreeNode node = new VisualTreeNodeImpl(null);
		node.setAdditionalParameter("test", "abc");
		float f = factory.getFloatFromVisualTreeNode("test", node);
		assertThat(f).isEqualTo(0);
	}
}