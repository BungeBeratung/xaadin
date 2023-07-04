package com.xaadin.elementfactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.xaadin.VisualTreeNodeImpl;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractOrderedLayoutElementFactoryTest {

    private AbstractOrderedLayoutElementFactory factory;

    @BeforeEach
    public void setUp() throws Exception {
        factory = Mockito.mock(AbstractOrderedLayoutElementFactory.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testAddComponentToParentDefaultExpandRatio() throws Exception {
        HorizontalLayout layout = new HorizontalLayout();

        Button button = new Button();
        VisualTreeNodeImpl buttonVisualTreeNode = new VisualTreeNodeImpl(button);
        buttonVisualTreeNode.setAdditionalParameter("alignment", "BOTTOM_RIGHT");

        factory.addComponentToParent(new VisualTreeNodeImpl(layout), buttonVisualTreeNode);

        assertThat(layout.getComponentCount()).isEqualTo(1);
        assertThat(layout.getComponent(0)).isSameAs(button);
        assertThat(layout.getComponentAlignment(button)).isEqualTo(Alignment.BOTTOM_RIGHT);
        assertThat(layout.getExpandRatio(button)).isEqualTo(0.f);
    }

    @Test
    public void testAddComponentToParent() throws Exception {
        HorizontalLayout layout = new HorizontalLayout();

        Button button = new Button();
        VisualTreeNodeImpl buttonVisualTreeNode = new VisualTreeNodeImpl(button);
        buttonVisualTreeNode.setAdditionalParameter("alignment", "BOTTOM_RIGHT");
        buttonVisualTreeNode.setAdditionalParameter("expandRatio", "0.65");

        factory.addComponentToParent(new VisualTreeNodeImpl(layout), buttonVisualTreeNode);

        assertThat(layout.getComponentCount()).isEqualTo(1);
        assertThat(layout.getComponent(0)).isSameAs(button);
        assertThat(layout.getComponentAlignment(button)).isEqualTo(Alignment.BOTTOM_RIGHT);
        assertThat(layout.getExpandRatio(button)).isEqualTo(0.65f, Offset.offset(0.001f));
    }

    @Test
    public void testIsClassSupportedForElementFactory() throws Exception {
        assertThat(factory.isClassSupportedForElementFactory(HorizontalLayout.class.getName()));
        assertThat(factory.isClassSupportedForElementFactory(VerticalLayout.class.getName()));
    }
}
