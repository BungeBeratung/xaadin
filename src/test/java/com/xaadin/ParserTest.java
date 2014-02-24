package com.xaadin;

import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

public class ParserTest {

	public static final String PARSER_TEST_ALL_COMPONENTS = "ParserTestAllComponents.xml";
    public static final String PARSER_TEST_DYNAMIC_ENUM = "ParserTestEnumProperty.xml";

    @Test
    public void shouldParseAllComponentsInGivenXml() throws Exception {
		URL url = ClassLoader.getSystemResource(PARSER_TEST_ALL_COMPONENTS);
		VisualTreeNode visualTreeNode = Parser.parse(url, null);

		for (Class clazz : TestConstants.SUPPORTED_COMPONENTS) {
			String className = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
			Component component = visualTreeNode.findComponentById(className);

			assertThat(component).isOfAnyClassIn(clazz);
		}
	}

    @Test
    public void testDynamicEnums() throws Exception {
        URL url = ClassLoader.getSystemResource(PARSER_TEST_DYNAMIC_ENUM);
        VisualTreeNode visualTreeNode = Parser.parse(url, null);

        DateField fieldSecond = visualTreeNode.findComponentById("dateFieldSecond");
        DateField fieldMonth = visualTreeNode.findComponentById("dateFieldMonth");
        assertThat(fieldSecond.getResolution()).isEqualTo(Resolution.SECOND);
        assertThat(fieldMonth.getResolution()).isEqualTo(Resolution.MONTH);
    }
}
