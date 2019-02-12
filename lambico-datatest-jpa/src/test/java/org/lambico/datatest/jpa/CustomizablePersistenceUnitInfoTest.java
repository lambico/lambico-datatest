package org.lambico.datatest.jpa;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class CustomizablePersistenceUnitInfoTest {
    @Test
    public void testBuildWithAllDefaults() {
        CustomizablePersistenceUnitInfo pui = CustomizablePersistenceUnitInfo.builder().build();
        assertThat(pui, is(not(nullValue())));
        assertThat(pui.getPersistenceXMLSchemaVersion(), is(CustomizablePersistenceUnitInfo.DEFAULT_JPA_VERSION));
        assertThat(pui.getPersistenceProviderClassName(), is(CustomizablePersistenceUnitInfo.DEFAULT_PERSISTENCE_PROVIDER_CLASS_NAME));
        assertThat(pui.getPersistenceUnitName(), is(CustomizablePersistenceUnitInfo.DEFAULT_PERSISTENCE_UNIT_NAME));
        assertThat(pui.getProperties(), is(not(nullValue())));
    }
}