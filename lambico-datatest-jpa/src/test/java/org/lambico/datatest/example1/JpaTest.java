package org.lambico.datatest.example1;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.lambico.datatest.example1.model.Entity1;
import org.lambico.datatest.example1.model.Entity2;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.json.SingleJsonDatasetLoader;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.junit.JpaContext;

public class JpaTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder()
        .datasetLoader(
            SingleJsonDatasetLoader.builder()
            .datasetResource("org/lambico/datatest/example1/dataset/dataset.json")
            .build())
        .entityManagerFactory(
            EntityManagerFactoryCreator.builder()
            .jpaProperty("hibernate.show_sql", "true")
            .entity(Entity1.class).entity(Entity2.class)
            .build())
        .build();

    @Rule
    public JpaContext jpaContext = new JpaContext(dataset.getEntityManagerFactory());

    @Test
    public void findObjects() {
        EntityManager em = this.jpaContext.getEntityManager();
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

