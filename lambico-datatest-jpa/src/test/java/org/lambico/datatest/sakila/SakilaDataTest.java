package org.lambico.datatest.sakila;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.annotation.JpaTest;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.junit.Dataset;
import org.lambico.datatest.junit.JpaContext;
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

@TestData(resource="org/lambico/datatest/sakila/dataset/sakila.json")
@JpaTest(loadEntities = {Country.class, City.class, Address.class, Store.class,
                    Staff.class, Customer.class, Language.class, Actor.class,
                    Film.class, FilmActor.class, Inventory.class, Rental.class,
                    Payment.class, Category.class, FilmCategory.class},
         useMerge = true)
public class SakilaDataTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Rule
    public JpaContext jpaContext = new JpaContext(dataset.getEntityManagerFactory());

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
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Film> query = em.createQuery("select f from Film f", Film.class);
        List<Film> films = query.getResultList();
        assertThat(films, hasSize(1000));
        Film film = films.get(0);
        assertThat(film, is(not(nullValue())));
    }

    @Test
    public void findAllActors() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Actor> query = em.createQuery("select a from Actor a", Actor.class);
        List<Actor> actors = query.getResultList();
        assertThat(actors, hasSize(200));
        Actor actor = actors.get(0);
        assertThat(actor, is(not(nullValue())));
    }

    @Test
    public void findAllStores() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Store> query = em.createQuery("select s from Store s", Store.class);
        List<Store> stores = query.getResultList();
        assertThat(stores, hasSize(2));
        Store store = stores.get(0);
        assertThat(store, is(not(nullValue())));
    }

    @Test
    public void findAllStaff() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Staff> query = em.createQuery("select s from Staff s", Staff.class);
        List<Staff> allStaff = query.getResultList();
        assertThat(allStaff, hasSize(2));
        Staff staff = allStaff.get(0);
        assertThat(staff, is(not(nullValue())));
    }

    @Test
    public void findAllAddresses() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Address> query = em.createQuery("select a from Address a", Address.class);
        List<Address> addresses = query.getResultList();
        assertThat(addresses, hasSize(603));
        Address address = addresses.get(0);
        assertThat(address, is(not(nullValue())));
    }

    @Test
    public void findAllCities() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<City> query = em.createQuery("select c from City c", City.class);
        List<City> cities = query.getResultList();
        assertThat(cities, hasSize(600));
        City city = cities.get(0);
        assertThat(city, is(not(nullValue())));
    }

    @Test
    public void findAllCustomers() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Customer> query = em.createQuery("select c from Customer c", Customer.class);
        List<Customer> customers = query.getResultList();
        assertThat(customers, hasSize(599));
        Customer customer = customers.get(0);
        assertThat(customer, is(not(nullValue())));
    }

    @Test
    public void findAllRentals() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Rental> query = em.createQuery("select r from Rental r", Rental.class);
        List<Rental> rentals = query.getResultList();
        assertThat(rentals, hasSize(16044));
        Rental rental = rentals.get(0);
        assertThat(rental, is(not(nullValue())));
    }

    @Test
    public void findAllPayments() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Payment> query = em.createQuery("select p from Payment p", Payment.class);
        List<Payment> payments = query.getResultList();
        assertThat(payments, hasSize(16049));
        Payment payment = payments.get(0);
        assertThat(payment, is(not(nullValue())));
    }

}
