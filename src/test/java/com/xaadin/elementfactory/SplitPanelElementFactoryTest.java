package com.xaadin.elementfactory;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalSplitPanel;
import com.xaadin.VisualTreeNode;
import com.xaadin.VisualTreeNodeImpl;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SplitPanelElementFactoryTest {

    private SplitPanelElementFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new SplitPanelElementFactory();
    }

    @Test
    public void testAddComponentToParent() throws Exception {
        Button btn1 = new Button();
        Button btn2 = new Button();

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        VisualTreeNode splitPanelVisualTreeNode = new VisualTreeNodeImpl(panel);

        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn1));
        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn2));

        assertThat(panel.getComponentCount()).isEqualTo(2);
        assertThat(panel.getFirstComponent()).isSameAs(btn1);
        assertThat(panel.getSecondComponent()).isSameAs(btn2);
    }

    @Test
    public void testAddComponentToParentWithExpandRatio() throws Exception {
        Button btn1 = new Button();
        Button btn2 = new Button();

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        VisualTreeNode splitPanelVisualTreeNode = new VisualTreeNodeImpl(panel);
        splitPanelVisualTreeNode.setAdditionalParameter("splitPosition", "0.65");

        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn1));
        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn2));

        assertThat(panel.getComponentCount()).isEqualTo(2);
        assertThat(panel.getFirstComponent()).isSameAs(btn1);
        assertThat(panel.getSecondComponent()).isSameAs(btn2);
        assertThat(panel.getSplitPositionUnit()).isEqualTo(Sizeable.Unit.PERCENTAGE);
        assertThat(panel.getSplitPosition()).isEqualTo(0.65f * 100.f, Offset.offset(0.01f));
    }

    @Test
    public void testAddComponentToParentMoreThanTwoComponents() throws Exception {
        Button btn1 = new Button();
        Button btn2 = new Button();
        Button btn3 = new Button();

        HorizontalSplitPanel panel = new HorizontalSplitPanel();
        VisualTreeNode splitPanelVisualTreeNode = new VisualTreeNodeImpl(panel);

        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn1));
        factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn2));
		Assertions.assertThrows(ElementFactoryException.class, () -> factory.addComponentToParent(splitPanelVisualTreeNode, new VisualTreeNodeImpl(btn3)));
    }


    @Test
    public void testIsClassSupportedForElementFactory() throws Exception {
        assertThat(factory.isClassSupportedForElementFactory(HorizontalSplitPanel.class.getName()));
        assertThat(factory.isClassSupportedForElementFactory(VerticalSplitPanel.class.getName()));
    }
}
