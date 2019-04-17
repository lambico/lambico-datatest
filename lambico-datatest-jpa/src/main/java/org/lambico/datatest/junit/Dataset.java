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
package org.lambico.datatest.junit;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;
import org.lambico.datatest.DatasetLoaderFactory;
import org.lambico.datatest.annotation.JpaTest;
import org.lambico.datatest.annotation.Property;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator.EntityManagerFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;

public class Dataset implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(Dataset.class);

    private DatasetLoader datasetLoader;
    private DataAggregator dataAggregator;
    private EntityManagerFactory entityManagerFactory;
    private Class<?>[] loadEntities;
    protected Integer flushWindowSize;
    protected boolean useMerge;

    @Builder
    private Dataset(DatasetLoader datasetLoader, EntityManagerFactory entityManagerFactory, Class<?>[] loadEntities,
            Integer flushWindowSize, boolean useMerge) {
        this.datasetLoader = datasetLoader;
        this.entityManagerFactory = entityManagerFactory;
        this.loadEntities = loadEntities;
        if (flushWindowSize != null) {
            this.flushWindowSize = flushWindowSize;
        }
        this.useMerge = useMerge;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        completeWithAnnotations(description);
        return statement(base, description);
    }

    private void completeWithAnnotations(Description description) {
        if (this.datasetLoader == null) {
            TestData testData = description.getAnnotation(TestData.class);
            this.datasetLoader = DatasetLoaderFactory.create(testData);
        }
        JpaTest jpaTest = description.getAnnotation(JpaTest.class);
        if (jpaTest != null) {
            if (jpaTest.loadEntities().length > 0) {
                this.loadEntities = jpaTest.loadEntities();
            }
            if (jpaTest.flushWindowSize() > 0) {
                this.flushWindowSize = jpaTest.flushWindowSize();
            }
            this.useMerge = jpaTest.useMerge();
            if (this.entityManagerFactory == null) {
                EntityManagerFactoryBuilder builder = EntityManagerFactoryCreator.builder();
                if (jpaTest.entities().length > 0) {
                    for (Class<?> entity : jpaTest.entities()) {
                        builder.entity(entity);
                    }
                } else {
                    for (Class<?> entity : jpaTest.loadEntities()) {
                        builder.entity(entity);
                    }
                }
                for (Property prop : jpaTest.properties()) {
                    builder.jpaProperty(prop.name(), prop.value());
                }
                this.entityManagerFactory = builder.build();
            }
        }
    }

    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before(description);
                try {
                    base.evaluate();
                } finally {
                    after(description);
                }
            }
        };
    }

    protected void before(Description description) throws Throwable {
        this.dataAggregator = this.datasetLoader.load();
        if (this.entityManagerFactory != null) {
            this.populateTestDatabase();
        }
    }

    protected void after(Description description) {
        if (this.entityManagerFactory != null) {
            this.entityManagerFactory.close();
        }
    }

    /**
     * @return the dataAggregator
     */
    public DataAggregator getDataAggregator() {
        return dataAggregator;
    }

    /**
     * @return the entityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    protected void populateTestDatabase() {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Query disableReferentialIntegrity = em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE;");
        disableReferentialIntegrity.executeUpdate();
        Class<?>[] entities = this.loadEntities;
        if (entities == null) {
            entities = this.dataAggregator.getTypes().toArray(new Class<?>[0]);
        }
        int i = 1;
        for (Class<?> currentEntity : entities) {
            Collection<?> entityList = this.dataAggregator.getObjects().get(currentEntity.getName());
            for (Object entity : entityList) {
                log.debug("({}) Persisting entity {}", i, entity);
                if (this.useMerge) {
                    em.merge(entity);
                } else {
                    em.persist(entity);
                }
                if (this.flushWindowSize != null && i % this.flushWindowSize == 0) {
                    em.flush();
                    em.clear();
                }
                i++;
            }
        }
        Query enableReferentialIntegrity = em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE;");
        enableReferentialIntegrity.executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

}
