/**
 * Copyright © 2018 The Lambico Datatest Team (lucio.benfante@gmail.com)
 *
 * This file is part of lambico-datatest-jpa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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