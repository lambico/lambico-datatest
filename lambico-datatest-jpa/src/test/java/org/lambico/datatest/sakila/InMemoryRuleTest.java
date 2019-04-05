package org.lambico.datatest.sakila;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.json.SingleJsonDatasetLoader;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.sakila.model.Address;
import org.lambico.datatest.sakila.model.Film;

public class InMemoryRuleTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder()
        .datasetLoader(
            SingleJsonDatasetLoader.builder()
            .datasetResource("org/lambico/datatest/sakila/dataset/sakila.json")
            .build())
        .build();

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

