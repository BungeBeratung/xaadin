package com.xaadin.elementfactory;

import com.xaadin.TestConstants;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Hendrik Jürgens on 27.01.14.
 */
public class DefaultElementFactoryTest {

    private DefaultElementFactory factory;

    @Before
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
