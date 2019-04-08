/**
 * Copyright © 2018 The Lambico Datatest Team (lucio.benfante@gmail.com)
 *
 * This file is part of lambico-datatest-jpa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lambico.datatest.sakila;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Type.PersistenceType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.DataAggregator;
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

@Ignore
public class PostgresqlSakilaDumperTest {
    private static final Logger log = LoggerFactory.getLogger(PostgresqlSakilaDumperTest.class);
    private static EntityManagerFactory emf;

    @BeforeClass
    public static void initTestClass() {
        emf = EntityManagerFactoryCreator.builder()
                .jpaProperty("javax.persistence.jdbc.driver", "org.postgresql.Driver")
                .jpaProperty("javax.persistence.jdbc.url", "jdbc:postgresql://localhost/sakila")
                .jpaProperty("javax.persistence.jdbc.user", "sakila")
                .jpaProperty("javax.persistence.jdbc.password", "sakilaPwd1").entity(Actor.class).entity(Address.class)
                .entity(Category.class).entity(City.class).entity(Country.class).entity(Customer.class)
                .entity(Film.class).entity(FilmActor.class).entity(FilmCategory.class).entity(Inventory.class)
                .entity(Language.class).entity(Payment.class).entity(Rental.class).entity(Staff.class)
                .entity(Store.class).build();
    }

    @AfterClass
    public static void destroyTestClass() {
        emf.close();
    }

    @Test
    public void testSnapshotToJson() throws ClassNotFoundException, SQLException, IOException {
        DataAggregator dataAggregator = new DataAggregator();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Set<ManagedType<?>> managedTypes = em.getMetamodel().getManagedTypes();
        for (ManagedType<?> managedType : managedTypes) {
            if (managedType.getPersistenceType() == PersistenceType.ENTITY) {
                Class<?> entityType = managedType.getJavaType();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<?> cq = cb.createQuery(entityType);
                @SuppressWarnings("rawtypes")
                Root rootEntry = cq.from(entityType);
                @SuppressWarnings("unchecked")
                CriteriaQuery<?> all = cq.select(rootEntry);
                TypedQuery<?> allQuery = em.createQuery(all);
                dataAggregator.getObjects().put(entityType.getName(), allQuery.getResultList());
            }
        }
        assertThat(dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film"), hasSize(1000));
        Film firstFilm = (Film) dataAggregator.getObjects().get("org.lambico.datatest.sakila.model.Film").iterator()
                .next();
        Language firstFilmLanguage = firstFilm.getLanguage();

        assertThat(firstFilmLanguage.getFilmsInThisLanguage(), hasItem(firstFilm));
        ObjectMapper mapper = new ObjectMapper();
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        hibernate5Module.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        mapper.registerModule(hibernate5Module);
        File basePath = new File(System.getProperty("user.home"));
        File outputFile = new File(basePath, "sakila.json");
        log.info("Writing Sakila in JSON formato to " + outputFile.getAbsolutePath());
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, dataAggregator.getObjects());
        em.getTransaction().rollback();
        em.close();
    }

}
