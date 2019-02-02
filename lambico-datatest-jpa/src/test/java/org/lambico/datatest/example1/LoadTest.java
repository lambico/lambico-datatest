package org.lambico.datatest.example1;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.json.DataAggregator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LoadTest {

    @Test
    public void loadAggregatedObject() throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
        InputStream jsonSource = LoadTest.class.getClassLoader()
                .getResourceAsStream("org/lambico/datatest/example1/dataset/dataset.json");
        assertNotNull(jsonSource);
        DataAggregator dataAggregator = mapper.readValue(jsonSource, DataAggregator.class);
        assertTrue(dataAggregator instanceof DataAggregator);
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertThat(entity1.getStringField()).isEqualTo("test1");
        assertThat(entity2.getStringField()).isEqualTo("test2");
        assertThat(entity2).isEqualTo(entity1.getEntity2());
        assertThat(entity1).isEqualTo(entity2.getEntity1());

    }
    
}
