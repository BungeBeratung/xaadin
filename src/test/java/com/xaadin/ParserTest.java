package com.xaadin;

import com.vaadin.ui.*;
import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {

	public static final String PARSER_TEST_ALL_COMPONENTS = "ParserTestAllComponents.xml";

	@Test
	public void testParseAllComponents() throws Exception {
		Class[] componentsToCheck = {
			Label.class,
			TextField.class,
			Button.class,
			ComboBox.class,
			Table.class,
			ProgressBar.class,
			TextArea.class,
			Panel.class,
			TabSheet.class,
			VerticalLayout.class,
			HorizontalLayout.class,
			GridLayout.class
		};

		URL url = ClassLoader.getSystemResource(PARSER_TEST_ALL_COMPONENTS);
		VisualTreeNode visualTreeNode = Parser.parse(url, null);

		for (Class clazz : componentsToCheck) {
			String className = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
			Component component = visualTreeNode.findComponentById(className);

			assertThat(component).isNotNull();
			assertThat(component).isOfAnyClassIn(clazz);
		}
	}

}
