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
package org.lambico.datatest.json;

import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;


//@Ignore
public class MultipleJsonDatasetLoaderTest {

    private static final String MULTIPLE_DATASET_RESOURCE = "org/lambico/datatest/multiplejson";
    private static final String MULTIPLE_DATASET_RESOURCE_FROM_JAR = "lambico/multiple";

    @Test
    public void load_FromFolder() {
        DatasetLoader loader =
                MultipleJsonDatasetLoader.builder()
                        .datasetResourcePath(MULTIPLE_DATASET_RESOURCE)
                        .build();
        DataAggregator dataAggregator = loader.load();
        commonCheck(dataAggregator);
    }

    @Test
    public void load_FromJar() {
        DatasetLoader loader =
                MultipleJsonDatasetLoader.builder()
                        .datasetResourcePath(MULTIPLE_DATASET_RESOURCE_FROM_JAR)
                        .build();
        assertThat(loader, notNullValue());
        DataAggregator dataAggregator = loader.load();
        commonCheck(dataAggregator);
    }

    private void commonCheck(DataAggregator dataAggregator) {
        assertThat(dataAggregator.getObjects().keySet(), hasSize(2));
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        assertThat(entities1, is(notNullValue()));
        assertThat(entities1, hasSize(1));
        Entity1 entity1 = ((Entity1) entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2) entities2.iterator().next());
        assertThat(entity1.getStringField(), is("test1"));
        assertThat(entity2.getStringField(), is("test2"));
        assertThat(entity1.getEntity2(), is(sameInstance(entity2)));
        assertThat(entity2.getEntity1(), is(sameInstance(entity1)));
    }
}