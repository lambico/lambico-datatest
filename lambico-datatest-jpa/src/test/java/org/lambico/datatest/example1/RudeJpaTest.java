package org.lambico.datatest.example1;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.junit.Dataset;

public class RudeJpaTest {
    private static EntityManagerFactory emf;

    @ClassRule
    public static Dataset dataset = new Dataset("org/lambico/datatest/example1/dataset/dataset.json");

    private EntityManager em;

    @BeforeClass
    public static void initTestClass() {
        emf = EntityManagerFactoryCreator.builder()
            .jpaProperty("javax.persistence.schema-generation.database.action", "drop-and-create")
            .jpaProperty("hibernate.show_sql", "true")
            .entity(Entity1.class)
            .entity(Entity2.class)
            .build();
        populateTestDatabase();
    }

    private static void populateTestDatabase() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        for (Collection<?> entityList : dataset.getDataAggregator().getObjects().values()) {
            for (Object entity : entityList) {
                em.persist(entity);
            }
        }
        em.getTransaction().commit();
        em.close();
    }

    @AfterClass
    public static void destroyTestClass() {
        emf.close();
    }

    @Before
    public void initTest() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void destroyTest() {
        em.getTransaction();
        em.close();
    }

    @Test
    public void findObjects() throws IOException, ClassNotFoundException {
        TypedQuery<Entity1> query = em.createQuery("select e1 from Entity1 e1", Entity1.class);
        List<Entity1> results = query.getResultList();
        assertThat(results.size(), is(1));
        Entity1 entity1 = results.get(0);
        assertThat(entity1.getStringField(), is("test1"));
        Entity2 entity2 = entity1.getEntity2();
        assertThat(entity2.getStringField(), is("test2"));
        assertThat(entity2.getEntity1(), is(sameInstance(entity1)));
    }

}
