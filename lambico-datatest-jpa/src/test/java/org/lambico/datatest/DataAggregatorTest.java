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
package org.lambico.datatest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;

public class DataAggregatorTest {

    @Test
    public void testGetTypes() {
        DataAggregator instance = new DataAggregator();
        Map<String, Collection<?>> objects = instance.getObjects();
        objects.put("org.lambico.datatest.example1.model.Entity1", null);
        objects.put("org.lambico.datatest.example1.model.Entity2", null);
        Set<Class<?>> types = instance.getTypes();
        assertThat(types, is(notNullValue()));
        assertThat(types, containsInAnyOrder(Entity1.class, Entity2.class));
    }
}