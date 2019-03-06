package org.lambico.datatest.json;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collection;

import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.sakila.model.Address;
import org.lambico.datatest.sakila.model.Film;

public class SingleJsonDatasetLoaderTest {

    private static final String SIMPLE_DATASET_RESOURCE = "org/lambico/datatest/example1/dataset/dataset.json";
    private static final String SAKILA_DATASET_RESOURCE = "org/lambico/datatest/sakila/dataset/sakila.json";

    @Test
    public void testLoadingSimpleDataset() {
        DatasetLoader loader =
            SingleJsonDatasetLoader.builder()
                .datasetResource(SIMPLE_DATASET_RESOURCE)
                .build();
        DataAggregator dataAggregator = loader.load();
        assertThat(dataAggregator.getObjects().keySet(), hasSize(2));
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        assertThat(entities1, is(notNullValue()));
        assertThat(entities1, hasSize(1));
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertThat(entity1.getStringField(), is("test1"));
        assertThat(entity2.getStringField(), is("test2"));
        assertThat(entity1.getEntity2(), is(sameInstance(entity2)));
        assertThat(entity2.getEntity1(), is(sameInstance(entity1)));
    }

    @Test
    public void testLoadingSakilaDataset() {
        DatasetLoader loader =
            SingleJsonDatasetLoader.builder()
                .datasetResource(SAKILA_DATASET_RESOURCE)
                .build();
        DataAggregator dataAggregator = loader.load();
        assertThat(dataAggregator.getObjects().entrySet().size(), is(15));
        Collection<?> films = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film");
        assertThat(films.size(), is(1000));
        assertThat(films.iterator().next(), is(instanceOf(Film.class)));
        Collection<?> addresses = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Address");
        assertThat(addresses.size(), is(603));
        assertThat(addresses.iterator().next(), is(instanceOf(Address.class)));
    }
}
