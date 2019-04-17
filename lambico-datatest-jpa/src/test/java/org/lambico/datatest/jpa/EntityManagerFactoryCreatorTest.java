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

import org.junit.Test;

import javax.persistence.EntityManagerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class EntityManagerFactoryCreatorTest {
    @Test
    public void testWithAllDefaults() throws IllegalArgumentException,
            SecurityException {
        EntityManagerFactory emf = EntityManagerFactoryCreator.builder().build();
        assertThat(emf, is(not(nullValue())));
        assertThat(emf.getProperties().keySet(),
                hasItems("javax.persistence.schema-generation.database.action",
                        "hibernate.enable_lazy_load_no_trans", "hibernate.event.merge.entity_copy_observer"));
        assertThat(emf.getProperties().get("javax.persistence.schema-generation.database.action"), is("drop-and-create"));
        assertThat(emf.getProperties().get("hibernate.enable_lazy_load_no_trans"), is("true"));
        assertThat(emf.getProperties().get("hibernate.event.merge.entity_copy_observer"), is("allow"));
        emf.close();
    }

}