package com.xaadin.elementfactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.xaadin.VisualTreeNodeImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Hendrik Jürgens on 27.01.14.
 */
public class AbstractDefaultLayoutElementFactoryTest {

    private AbstractDefaultLayoutElementFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = Mockito.mock(AbstractDefaultLayoutElementFactory.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testAddComponentToParent() throws Exception {
        HorizontalLayout layout = new HorizontalLayout();

        Button button = new Button();
        VisualTreeNodeImpl buttonVisualTreeNode = new VisualTreeNodeImpl(button);
        buttonVisualTreeNode.setAdditionalParameter("alignment", "BOTTOM_RIGHT");

        factory.addComponentToParent(new VisualTreeNodeImpl(layout), buttonVisualTreeNode);

        assertThat(layout.getComponentCount()).isEqualTo(1);
        assertThat(layout.getComponent(0)).isSameAs(button);
        assertThat(layout.getComponentAlignment(button)).isEqualTo(Alignment.BOTTOM_RIGHT);
    }
}
