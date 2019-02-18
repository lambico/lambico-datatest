package org.lambico.datatest.example1;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.json.DataAggregator;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.junit.DatasetPath;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class DatasetPathTest {
    @Rule
    public DatasetPath datasetPath = new DatasetPath("org.lambico.datatest.example1.multidataset");




    @Test
    public void loadAggregatedObject() {
        //when
        DataAggregator dataAggregator = datasetPath.getDataAggregator();

        //then
        assertTrue(dataAggregator instanceof DataAggregator);
        Collection<?> entities1 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity1");
        Entity1 entity1 = ((Entity1)entities1.iterator().next());
        Collection<?> entities2 = dataAggregator.getObjects().get("org.lambico.datatest.example1.model.Entity2");
        Entity2 entity2 = ((Entity2)entities2.iterator().next());
        assertEquals("test1", entity1.getStringField());
        assertEquals("test2", entity2.getStringField());
        assertSame(entity2, entity1.getEntity2());
        assertSame(entity1, entity2.getEntity1());
    }
}