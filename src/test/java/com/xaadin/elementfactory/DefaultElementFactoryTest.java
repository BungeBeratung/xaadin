package com.xaadin.elementfactory;

import com.xaadin.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultElementFactoryTest {

    private DefaultElementFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new DefaultElementFactory("com.vaadin.ui");
    }

    @Test
    public void testIsClassSupportedForElementFactory() throws Exception {
        for (Class clazz : TestConstants.SUPPORTED_COMPONENTS) {
            assertThat(factory.isClassSupportedForElementFactory(clazz.getName()));
        }
    }
}
