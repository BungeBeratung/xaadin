package com.xaadin.elementfactory;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.xaadin.VisualTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class GridLayoutElementFactory extends AbstractDefaultElementFactory {

    private int getIntFromVisualTreeNode(String parameterName, VisualTreeNode node) {
        try {
            return Integer.parseInt(node.getAdditionalParameter(parameterName, "-1"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected List<Float> parseExpandRatios(String ratios) throws ElementFactoryException {
        if (ratios == null || ratios.isEmpty()) {
            return Collections.emptyList();
		}

        final StringTokenizer tokenizer = new StringTokenizer(ratios, ",");
        List<Float> tokens = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
			final String token = tokenizer.nextToken();
			try {
				tokens.add(Float.parseFloat(token));
            } catch (NumberFormatException e) {
                throw new ElementFactoryException("invalid token or expandRatio: '" + ratios + "' invalid token: '" + token + "'", e);
            }
        }
        return tokens;
    }

    public void addComponentToParent(VisualTreeNode parent, VisualTreeNode child) throws ElementFactoryException {
        if (!(parent.getComponent() instanceof GridLayout)) {
            throw new IllegalArgumentException("parent is not a descendent of com.vaadin.ui.GridLayout");
        }

        GridLayout gridLayout = parent.getComponent();

		int row = getIntFromVisualTreeNode("grid.row", child);
		int col = getIntFromVisualTreeNode("grid.column", child);
		int rowSpan = getIntFromVisualTreeNode("grid.rowSpan", child);
		int colSpan = getIntFromVisualTreeNode("grid.columnSpan", child);

		if (rowSpan <= 0) {
            rowSpan = 1;
        }
        if (colSpan <= 0) {
            colSpan = 1;
        }

        // use automatic ordering
        if ((row < 0) && (col < 0)) {
            int currentRow = getIntFromVisualTreeNode("GridLayoutElementFactory.currentRowIndex", parent);
            int currentCol = getIntFromVisualTreeNode("GridLayoutElementFactory.currentColIndex", parent);
            if (currentRow < 0) {
                currentRow = 0;
            }
            if (currentCol < 0) {
                currentCol = 0;
            }

            row = currentRow;
            col = currentCol;

            for (int i = 0; i < colSpan; i++) {
                currentCol++;
                if (currentCol >= gridLayout.getColumns()) {
                    currentCol = 0;
                    currentRow++;
                }
            }
            parent.setAdditionalParameter("GridLayoutElementFactory.currentRowIndex", Integer.toString(currentRow));
            parent.setAdditionalParameter("GridLayoutElementFactory.currentColIndex", Integer.toString(currentCol));
        } else {
            if (row < 0)
                row = 0;
            if (col < 0)
                col = 0;
        }

        Alignment alignment = parseAlignment(child.getAdditionalParameter("alignment", ""));

        List<Float> rowExpandRatios = parseExpandRatios(parent.getAdditionalParameter("rowExpandRatio", ""));
		List<Float> columnExpandRatios = parseExpandRatios(parent.getAdditionalParameter("columnExpandRatio", ""));

        if ((row + (rowSpan - 1)) >= gridLayout.getRows())
            gridLayout.setRows(row + (rowSpan - 1) + 1);
        if ((col + (colSpan - 1)) >= gridLayout.getColumns())
            gridLayout.setColumns(col + (colSpan - 1) + 1);

        Component component = child.getComponent();
        if ((rowSpan > 1) || (colSpan > 1)) {
            gridLayout.addComponent(component, col, row, col + Math.max(0, colSpan - 1), row + Math.max(0, rowSpan - 1));
        } else {
            gridLayout.addComponent(component, col, row);
        }
        gridLayout.setComponentAlignment(component, alignment);

        if (rowExpandRatios.size() > row) {
            gridLayout.setRowExpandRatio(row, rowExpandRatios.get(row));
        }
        if (columnExpandRatios.size() > col) {
            gridLayout.setColumnExpandRatio(col, columnExpandRatios.get(col));
        }

    }

    public boolean isClassSupportedForElementFactory(String classname) {
        return classname.equals(GridLayout.class.getName());
    }
}
