package org.lambico.datatest;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;

public class DataAggregatorTest {

    @Test
    public void testGetTypes() {
        DataAggregator instance = new DataAggregator();
        Map<String, Collection<?>> objects = instance.getObjects();
        objects.put("org.lambico.datatest.example1.model.Entity1", null);
        objects.put("org.lambico.datatest.example1.model.Entity2", null);
        Set<Class<?>> types = instance.getTypes();
        assertThat(types, is(notNullValue()));
        assertThat(types, containsInAnyOrder(Entity1.class, Entity2.class));
    }
}