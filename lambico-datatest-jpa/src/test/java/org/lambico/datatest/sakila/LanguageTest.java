package org.lambico.datatest.sakila;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
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
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.sakila.model.Actor;
import org.lambico.datatest.sakila.model.Address;
import org.lambico.datatest.sakila.model.Category;
import org.lambico.datatest.sakila.model.City;
import org.lambico.datatest.sakila.model.Country;
import org.lambico.datatest.sakila.model.Customer;
import org.lambico.datatest.sakila.model.Film;
import org.lambico.datatest.sakila.model.FilmActor;
import org.lambico.datatest.sakila.model.FilmCategory;
import org.lambico.datatest.sakila.model.Inventory;
import org.lambico.datatest.sakila.model.Language;
import org.lambico.datatest.sakila.model.Payment;
import org.lambico.datatest.sakila.model.Rental;
import org.lambico.datatest.sakila.model.Staff;
import org.lambico.datatest.sakila.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageTest {
    private static final Logger log = LoggerFactory.getLogger(LanguageTest.class);
    private static EntityManagerFactory emf;

    @ClassRule
    public static Dataset dataset = new Dataset("org/lambico/datatest/sakila/dataset/sakila.json");

    private EntityManager em;

    @BeforeClass
    public static void initTestClass() {
        emf = EntityManagerFactoryCreator.builder()
                .jpaProperty("javax.persistence.schema-generation.database.action", "drop-and-create")
                .jpaProperty("hibernate.enable_lazy_load_no_trans", "true")
                .jpaProperty("hibernate.event.merge.entity_copy_observer", "allow")
                // .jpaProperty("hibernate.show_sql", "true")
                .entity(Actor.class).entity(Address.class)
                .entity(Category.class).entity(City.class).entity(Country.class).entity(Customer.class)
                .entity(Film.class).entity(FilmActor.class).entity(FilmCategory.class).entity(Inventory.class)
                .entity(Language.class).entity(Payment.class).entity(Rental.class).entity(Staff.class)
                .entity(Store.class).build();
        populateTestDatabase();
    }

    private static void populateTestDatabase() {
        List<Class<?>> orderedEntities = new ArrayList<>();
        orderedEntities.add(Language.class);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        int i = 0;
        for (Class<?> currentEntity : orderedEntities) {
            Collection<?> entityList = dataset.getDataAggregator().getObjects().get(currentEntity.getName());
            for (Object entity : entityList) {
                log.debug("Persisting entity {}", entity);
                em.merge(entity);
                if (i % 100 == 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.flush();
            em.clear();
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
        em.getTransaction().rollback();
        em.close();
    }

    @Test
    public void findAllLanguages() {
        TypedQuery<Language> query = em.createQuery("select l from Language l order by l.id", Language.class);
        List<Language> languages = query.getResultList();
        assertThat(languages.size(), is(6));
        Language english = languages.get(0);
        assertThat(english, is(not(nullValue())));
        assertThat(english.getLanguageId(), is(1));
        assertThat(english.getName().trim(), is("English"));
    }

}
