package com.xaadin;

import com.vaadin.ui.*;
import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {

	public static final String PARSER_TEST_ALL_COMPONENTS = "ParserTestAllComponents.xml";

	private Class[] TEST_COMPONENTS_TO_PARSE = new Class[]{
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
            GridLayout.class,
            HorizontalSplitPanel.class,
            VerticalSplitPanel.class
    };

    @Test
	public void shouldParseAllComponentsInGivenXml() throws Exception {
		URL url = ClassLoader.getSystemResource(PARSER_TEST_ALL_COMPONENTS);
		VisualTreeNode visualTreeNode = Parser.parse(url, null);

		for (Class clazz : TEST_COMPONENTS_TO_PARSE) {
			String className = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
			Component component = visualTreeNode.findComponentById(className);

			assertThat(component).isOfAnyClassIn(clazz);
		}
	}

}
