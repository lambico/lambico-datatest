package org.lambico.datatest.sakila;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.DataAggregator;
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

public class RudeJpaTest {
    private static final Logger log = LoggerFactory.getLogger(RudeJpaTest.class);
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
                .entity(Actor.class).entity(Address.class).entity(Category.class).entity(City.class)
                .entity(Country.class).entity(Customer.class).entity(Film.class).entity(FilmActor.class)
                .entity(FilmCategory.class).entity(Inventory.class).entity(Language.class).entity(Payment.class)
                .entity(Rental.class).entity(Staff.class).entity(Store.class).build();
        populateTestDatabase();
    }

    private static void populateTestDatabase() {
        List<Class<?>> orderedEntities = new ArrayList<>();
        orderedEntities.add(Country.class);
        orderedEntities.add(City.class);
        orderedEntities.add(Address.class);
        orderedEntities.add(Store.class);
        orderedEntities.add(Staff.class);
        orderedEntities.add(Customer.class);

        orderedEntities.add(Language.class);
        orderedEntities.add(Actor.class);
        orderedEntities.add(Film.class);
        orderedEntities.add(FilmActor.class);
        orderedEntities.add(Inventory.class);

        orderedEntities.add(Rental.class);
        orderedEntities.add(Payment.class);
        
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Query disableReferentialIntegrity = em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE;");
        disableReferentialIntegrity.executeUpdate();
        int i = 1;
        for (Class<?> currentEntity : orderedEntities) {
            Collection<?> entityList = dataset.getDataAggregator().getObjects().get(currentEntity.getName());
            for (Object entity : entityList) {
                log.debug("({}) Persisting entity {}", i++, entity);
                em.merge(entity);
                if (i++ % 200 == 0) {
                    em.flush();
                    em.clear();
                }
            }
        }
        Query enableReferentialIntegrity = em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE;");
        enableReferentialIntegrity.executeUpdate();
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
    public void testInMemorySet() {
        DataAggregator dataAggregator = dataset.getDataAggregator();
        assertThat(dataAggregator.getObjects().entrySet().size(), is(15));
        Collection<?> films = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film");
        assertThat(films.size(), is(1000));
        assertThat(films.iterator().next(), is(instanceOf(Film.class)));
        Collection<?> addresses = dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Address");
        assertThat(addresses.size(), is(603));
        assertThat(addresses.iterator().next(), is(instanceOf(Address.class)));

    }

    @Test
    public void findAllFilms() {
        TypedQuery<Film> query = em.createQuery("select f from Film f", Film.class);
        List<Film> films = query.getResultList();
        assertThat(films, hasSize(1000));
        Film film = films.get(0);
        assertThat(film, is(not(nullValue())));
    }

    @Test
    public void findAllActors() {
        TypedQuery<Actor> query = em.createQuery("select a from Actor a", Actor.class);
        List<Actor> actors = query.getResultList();
        assertThat(actors, hasSize(200));
        Actor actor = actors.get(0);
        assertThat(actor, is(not(nullValue())));
    }

    @Test
    public void findAllStores() {
        TypedQuery<Store> query = em.createQuery("select s from Store s", Store.class);
        List<Store> stores = query.getResultList();
        assertThat(stores, hasSize(2));
        Store store = stores.get(0);
        assertThat(store, is(not(nullValue())));
    }

    @Test
    public void findAllStaff() {
        TypedQuery<Staff> query = em.createQuery("select s from Staff s", Staff.class);
        List<Staff> allStaff = query.getResultList();
        assertThat(allStaff, hasSize(2));
        Staff staff = allStaff.get(0);
        assertThat(staff, is(not(nullValue())));
    }

    @Test
    public void findAllAddresses() {
        TypedQuery<Address> query = em.createQuery("select a from Address a", Address.class);
        List<Address> addresses = query.getResultList();
        assertThat(addresses, hasSize(603));
        Address address = addresses.get(0);
        assertThat(address, is(not(nullValue())));
    }

    @Test
    public void findAllCities() {
        TypedQuery<City> query = em.createQuery("select c from City c", City.class);
        List<City> cities = query.getResultList();
        assertThat(cities, hasSize(600));
        City city = cities.get(0);
        assertThat(city, is(not(nullValue())));
    }

    @Test
    public void findAllCustomers() {
        TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);
        List<Customer> customers = query.getResultList();
        assertThat(customers, hasSize(599));
        Customer customer = customers.get(0);
        assertThat(customer, is(not(nullValue())));
    }

    @Test
    public void findAllRentals() {
        TypedQuery<Rental> query = em.createQuery("select r from Rental r", Rental.class);
        List<Rental> rentals = query.getResultList();
        assertThat(rentals, hasSize(16044));
        Rental rental = rentals.get(0);
        assertThat(rental, is(not(nullValue())));
    }

    @Test
    public void findAllPayments() {
        TypedQuery<Payment> query = em.createQuery("select p from Payment p", Payment.class);
        List<Payment> payments = query.getResultList();
        assertThat(payments, hasSize(16049));
        Payment payment = payments.get(0);
        assertThat(payment, is(not(nullValue())));
    }

}
