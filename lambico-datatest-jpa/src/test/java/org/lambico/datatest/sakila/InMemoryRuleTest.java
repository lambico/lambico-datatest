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
package org.lambico.datatest.sakila;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.sakila.model.Address;
import org.lambico.datatest.sakila.model.Film;

@TestData(resource="org/lambico/datatest/sakila/dataset/sakila.json")
public class InMemoryRuleTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Test
    public void loadAggregatedObject() {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        assertThat(dataAggregator.getObjects().entrySet().size(), is(15));
        Collection<?> films = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film");
        assertThat(films.size(), is(1000));
        assertThat(films.iterator().next(), is(instanceOf(Film.class)));
        Collection<?> addresses = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Address");
        assertThat(addresses.size(), is(603));
        assertThat(addresses.iterator().next(), is(instanceOf(Address.class)));
    }
    
}

