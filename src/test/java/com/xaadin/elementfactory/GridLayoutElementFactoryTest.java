package com.xaadin.elementfactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.xaadin.VisualTreeNode;
import com.xaadin.VisualTreeNodeImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Hendrik Jürgens on 27.01.14.
 */
public class GridLayoutElementFactoryTest {

    private GridLayoutElementFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new GridLayoutElementFactory();
    }

    @Test
    public void testParseExpandRatios() throws Exception {
        float[] expandRatios = factory.parseExpandRatios("0.1,0.2,0.3, 0.4,           0.5");
        assertThat(expandRatios).isEqualTo(new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f});
    }

    @Test
    public void testParseExpandRatiosEmpty() throws Exception {
        float[] expandRatios = factory.parseExpandRatios("");
        assertThat(expandRatios).isEqualTo(new float[]{});
    }

    @Test(expected = ElementFactoryException.class)
    public void testParseExpandRatiosInvalid() throws Exception {
        factory.parseExpandRatios("0.1, 0.2, asds");
    }

    @Test
    public void testAddComponentToParentFixedColumns() throws Exception {
        GridLayout layout = new GridLayout();
        layout.setColumns(2);

        VisualTreeNode node = new VisualTreeNodeImpl(layout);

        Button b1 = new Button();
        Button b2 = new Button();
        Button b3 = new Button();
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b1));
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b2));
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b3));
        assertThat(layout.getComponent(0, 0)).isSameAs(b1);
        assertThat(layout.getComponent(1, 0)).isSameAs(b2);
        assertThat(layout.getComponent(0, 1)).isSameAs(b3);
    }

    @Test
    public void testAddComponentToParentColspan() throws Exception {
        GridLayout layout = new GridLayout();
        layout.setColumns(2);

        VisualTreeNode node = new VisualTreeNodeImpl(layout);

        Button b1 = new Button();
        VisualTreeNode b1node = new VisualTreeNodeImpl(b1);
        b1node.setAdditionalParameter("columnSpan", "2");
        Button b2 = new Button();
        Button b3 = new Button();
        factory.addComponentToParent(node, b1node);
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b2));
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b3));
        assertThat(layout.getComponent(0, 0)).isSameAs(b1);
        assertThat(layout.getComponent(1, 0)).isSameAs(b1);
        assertThat(layout.getComponent(0, 1)).isSameAs(b2);
        assertThat(layout.getComponent(1, 1)).isSameAs(b3);
    }

    @Test
    @Ignore("TODO we need to fix this matter somehow... needs more thinking")
    public void testAddComponentToParentRowspan() throws Exception {
        GridLayout layout = new GridLayout();
        layout.setColumns(2);

        VisualTreeNode node = new VisualTreeNodeImpl(layout);

        Button b1 = new Button();
        VisualTreeNode b1node = new VisualTreeNodeImpl(b1);
        b1node.setAdditionalParameter("rowSpan", "2");
        Button b2 = new Button();
        Button b3 = new Button();
        factory.addComponentToParent(node, b1node);
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b2));
        factory.addComponentToParent(node, new VisualTreeNodeImpl(b3));
        assertThat(layout.getComponent(0, 0)).isSameAs(b1);
        assertThat(layout.getComponent(0, 1)).isSameAs(b1);
        assertThat(layout.getComponent(1, 0)).isSameAs(b2);
        assertThat(layout.getComponent(1, 1)).isSameAs(b3);
    }

    @Test
    public void testAddComponentToParentFixedItemPlaces() throws Exception {
        GridLayout layout = new GridLayout();
        VisualTreeNode node = new VisualTreeNodeImpl(layout);
        Button b1 = new Button();
        VisualTreeNode b1node = new VisualTreeNodeImpl(b1);
        b1node.setAdditionalParameter("row", "1");
        b1node.setAdditionalParameter("column", "1");
        Button b2 = new Button();
        VisualTreeNode b2node = new VisualTreeNodeImpl(b2);
        b2node.setAdditionalParameter("row", "2");
        b2node.setAdditionalParameter("column", "2");
        Button b3 = new Button();
        VisualTreeNode b3node = new VisualTreeNodeImpl(b3);
        b3node.setAdditionalParameter("row", "5");
        b3node.setAdditionalParameter("column", "3");
        factory.addComponentToParent(node, b1node);
        factory.addComponentToParent(node, b2node);
        factory.addComponentToParent(node, b3node);
        assertThat(layout.getRows()).isEqualTo(6);
        assertThat(layout.getColumns()).isEqualTo(4);
        assertThat(layout.getComponent(1, 1)).isSameAs(b1);
        assertThat(layout.getComponent(2, 2)).isSameAs(b2);
        assertThat(layout.getComponent(3, 5)).isSameAs(b3);
    }

    @Test
    public void testAddComponentToParentFixedItemPlacesColspan() throws Exception {
        GridLayout layout = new GridLayout();
        VisualTreeNode node = new VisualTreeNodeImpl(layout);
        Button b1 = new Button();
        VisualTreeNode b1node = new VisualTreeNodeImpl(b1);
        b1node.setAdditionalParameter("row", "1");
        b1node.setAdditionalParameter("column", "1");
        b1node.setAdditionalParameter("columnSpan", "3");
        Button b2 = new Button();
        VisualTreeNode b2node = new VisualTreeNodeImpl(b2);
        b2node.setAdditionalParameter("row", "2");
        b2node.setAdditionalParameter("column", "2");
        Button b3 = new Button();
        VisualTreeNode b3node = new VisualTreeNodeImpl(b3);
        b3node.setAdditionalParameter("row", "5");
        b3node.setAdditionalParameter("column", "3");
        factory.addComponentToParent(node, b1node);
        factory.addComponentToParent(node, b2node);
        factory.addComponentToParent(node, b3node);
        assertThat(layout.getRows()).isEqualTo(6);
        assertThat(layout.getColumns()).isEqualTo(4);
        assertThat(layout.getComponent(1, 1)).isSameAs(b1);
        assertThat(layout.getComponent(2, 1)).isSameAs(b1);
        assertThat(layout.getComponent(3, 1)).isSameAs(b1);
        assertThat(layout.getComponent(2, 2)).isSameAs(b2);
        assertThat(layout.getComponent(3, 5)).isSameAs(b3);
    }

    @Test
    public void testAddComponentToParentFixedItemPlacesRowspan() throws Exception {
        GridLayout layout = new GridLayout();
        VisualTreeNode node = new VisualTreeNodeImpl(layout);
        Button b1 = new Button();
        VisualTreeNode b1node = new VisualTreeNodeImpl(b1);
        b1node.setAdditionalParameter("row", "1");
        b1node.setAdditionalParameter("column", "1");
        b1node.setAdditionalParameter("rowSpan", "3");
        Button b2 = new Button();
        VisualTreeNode b2node = new VisualTreeNodeImpl(b2);
        b2node.setAdditionalParameter("row", "2");
        b2node.setAdditionalParameter("column", "2");
        Button b3 = new Button();
        VisualTreeNode b3node = new VisualTreeNodeImpl(b3);
        b3node.setAdditionalParameter("row", "5");
        b3node.setAdditionalParameter("column", "3");
        factory.addComponentToParent(node, b1node);
        factory.addComponentToParent(node, b2node);
        factory.addComponentToParent(node, b3node);
        assertThat(layout.getRows()).isEqualTo(6);
        assertThat(layout.getColumns()).isEqualTo(4);
        assertThat(layout.getComponent(1, 1)).isSameAs(b1);
        assertThat(layout.getComponent(1, 2)).isSameAs(b1);
        assertThat(layout.getComponent(1, 3)).isSameAs(b1);
        assertThat(layout.getComponent(2, 2)).isSameAs(b2);
        assertThat(layout.getComponent(3, 5)).isSameAs(b3);
    }

    @Test
    public void testIsClassSupportedForElementFactory() throws Exception {
        assertThat(factory.isClassSupportedForElementFactory(GridLayout.class.getName()));
    }
}
