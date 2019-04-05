package org.lambico.datatest.example1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import java.util.Collection;

import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.json.SingleJsonDatasetLoader;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.junit.Dataset;

public class InMemoryRuleTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder()
        .datasetLoader(
            SingleJsonDatasetLoader.builder()
            .datasetResource("org/lambico/datatest/example1/dataset/dataset.json")
            .build())
        .build();

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
