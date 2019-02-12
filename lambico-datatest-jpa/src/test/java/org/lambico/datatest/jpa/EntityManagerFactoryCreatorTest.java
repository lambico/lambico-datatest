package org.lambico.datatest.jpa;

import static org.hamcrest.MatcherAssert.*;

import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.*;

import javax.persistence.EntityManagerFactory;

import org.junit.Test;

public class EntityManagerFactoryCreatorTest {
    @Test
    public void testWithAllDefaults() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        EntityManagerFactory emf = EntityManagerFactoryCreator.builder().build();
        assertThat(emf, is(not(nullValue())));
        emf.close();
    }

}