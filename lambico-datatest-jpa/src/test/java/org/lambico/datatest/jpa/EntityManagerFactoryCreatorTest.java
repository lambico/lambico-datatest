package org.lambico.datatest.jpa;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

public class EntityManagerFactoryCreatorTest {
    @Test
    public void testWithAllDefaults() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        EntityManagerFactory emf = EntityManagerFactoryCreator.builder().build();
        assertThat(emf, is(not(nullValue())));
        assertThat(emf.getProperties().keySet(),
                hasItems("javax.persistence.schema-generation.database.action",
                        "hibernate.enable_lazy_load_no_trans", "hibernate.event.merge.entity_copy_observer"));
        assertThat(emf.getProperties().get("javax.persistence.schema-generation.database.action"), is("drop-and-create"));
        assertThat(emf.getProperties().get("hibernate.enable_lazy_load_no_trans"), is("true"));
        assertThat(emf.getProperties().get("hibernate.event.merge.entity_copy_observer"), is("allow"));
        emf.close();
    }

}