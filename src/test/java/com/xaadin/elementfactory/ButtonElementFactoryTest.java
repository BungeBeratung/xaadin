package com.xaadin.elementfactory;

import com.vaadin.ui.Button;
import com.xaadin.VisualTreeNode;
import com.xaadin.VisualTreeNodeImpl;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by hej on 27.01.14.
 */
public class ButtonElementFactoryTest {

    private ButtonElementFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ButtonElementFactory();
    }

    private class TestButtonHandler {

        int callCount = 0;

        @SuppressWarnings("UnusedDeclaration")
        public void onTestButtonClick(VisualTreeNode node, Button.ClickEvent event) {
            callCount++;
        }
    }

    @Test
    public void testProcessEvents() throws Exception {
        Button button = new Button();
        VisualTreeNode node = new VisualTreeNodeImpl(button);
        node.setAdditionalParameter("onClick", "onTestButtonClick");

        TestButtonHandler handler = new TestButtonHandler();
        factory.processEvents(node, handler);

        assertThat(handler.callCount).isEqualTo(0);
        button.click();
        assertThat(handler.callCount).isEqualTo(1);
    }

    @Test
    public void testIsClassSupportedForElementFactory() throws Exception {
        assertThat(factory.isClassSupportedForElementFactory(Button.class.getName()));
    }
}
