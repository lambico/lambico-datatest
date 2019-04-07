package org.lambico.datatest.jpa;

import org.junit.Test;

import javax.persistence.EntityManagerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class EntityManagerFactoryCreatorTest {
    @Test
    public void testWithAllDefaults() throws IllegalArgumentException,
            SecurityException {
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