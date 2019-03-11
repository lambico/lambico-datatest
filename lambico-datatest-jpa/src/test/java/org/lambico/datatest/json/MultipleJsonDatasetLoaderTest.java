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

public class MultipleJsonDatasetLoaderTest {
    private static final String MULTIPLE_DATASET_RESOURCE = "org/lambico/datatest/multiplejson";
    private static final String EMPTY_DATASET_RESOURCE = "org/lambico/datatest";
    private static final String NOT_EXISTING_FOLDER = "org/lambico/notafolder";

    @Test
    public void loading_Multiple_Dataset() {
        DatasetLoader loader =
                MultipleJsonDatasetLoader.builder()
                        .datasetResource(MULTIPLE_DATASET_RESOURCE)
                        .build();
        DataAggregator dataAggregator = loader.load();
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

    @Test
    public void loading_empty_folder() {
        DatasetLoader loader =
                MultipleJsonDatasetLoader.builder()
                        .datasetResource(EMPTY_DATASET_RESOURCE)
                        .build();
        DataAggregator dataAggregator = loader.load();
        assertThat(dataAggregator.getObjects().keySet(), hasSize(0));
    }

    @Test(expected = RuntimeException.class)
    public void loading_notExisting_folder() {
        DatasetLoader loader =
                MultipleJsonDatasetLoader.builder()
                        .datasetResource(NOT_EXISTING_FOLDER)
                        .build();
        loader.load();
    }

}