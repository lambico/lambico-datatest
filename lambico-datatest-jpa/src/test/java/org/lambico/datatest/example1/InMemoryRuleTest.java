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
package org.lambico.datatest.example1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.junit.Dataset;

@TestData(resources = "org/lambico/datatest/example1/dataset/dataset.json")
public class InMemoryRuleTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Test
    public void testEntity1() {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        assertEquals("test1", entity1.getStringField());
    }

    @Test
    public void testEntity2() {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertEquals("test2", entity2.getStringField());
    }

    @Test
    public void testCircularReferences() {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertSame(entity2, entity1.getEntity2());
        assertSame(entity1, entity2.getEntity1());
    }
    
}
