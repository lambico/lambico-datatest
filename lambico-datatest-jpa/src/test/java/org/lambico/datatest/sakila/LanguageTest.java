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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
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
@JpaTest(entities = {Country.class, City.class, Address.class, Store.class,
                    Staff.class, Customer.class, Language.class, Actor.class,
                    Film.class, FilmActor.class, Inventory.class, Rental.class,
                    Payment.class, Category.class, FilmCategory.class},
         loadEntities = Language.class,
         useMerge = true)
public class LanguageTest {

    @ClassRule
    public static Dataset dataset = Dataset.builder().build();

    @Rule
    public JpaContext jpaContext = new JpaContext(dataset.getEntityManagerFactory());

    @Test
    public void findAllLanguages() {
        EntityManager em = this.jpaContext.getEntityManager();
        TypedQuery<Language> query = em.createQuery("select l from Language l order by l.id", Language.class);
        List<Language> languages = query.getResultList();
        assertThat(languages.size(), is(6));
        Language english = languages.get(0);
        assertThat(english, is(not(nullValue())));
        assertThat(english.getLanguageId(), is(1));
        assertThat(english.getName().trim(), is("English"));
    }

}
