package org.lambico.datatest.sakila;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.lambico.datatest.json.DataAggregator;
import org.lambico.datatest.sakila.model.Address;
import org.lambico.datatest.sakila.model.Film;

public class LoadTest {

    @Test
    public void loadAggregatedObject() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        InputStream jsonSource = LoadTest.class.getClassLoader()
                .getResourceAsStream("org/lambico/datatest/sakila/dataset/sakila.json");
        assertThat(jsonSource, is(not(nullValue())));
        DataAggregator dataAggregator = mapper.readValue(jsonSource, DataAggregator.class);
        assertThat(dataAggregator.getObjects().entrySet().size(), is(15));
        Collection<?> films = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film");
        assertThat(films.size(), is(1000));
        assertThat(films.iterator().next(), is(instanceOf(Film.class)));
        Collection<?> addresses = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Address");
        assertThat(addresses.size(), is(603));
        assertThat(addresses.iterator().next(), is(instanceOf(Address.class)));

    }
    
}
