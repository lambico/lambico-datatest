package org.lambico.datatest.junit;

import lombok.Builder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.lambico.datatest.DataAggregator;
import org.lambico.datatest.DatasetLoader;
import org.lambico.datatest.annotation.JpaTest;
import org.lambico.datatest.annotation.Property;
import org.lambico.datatest.annotation.TestData;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator;
import org.lambico.datatest.jpa.EntityManagerFactoryCreator.EntityManagerFactoryBuilder;
import org.lambico.datatest.json.SingleJsonDatasetLoader;
import org.lambico.datatest.json.SingleJsonDatasetLoader.SingleJsonDatasetLoaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Collection;

public class Dataset implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(Dataset.class);

    private DatasetLoader datasetLoader;
    private DataAggregator dataAggregator;
    private EntityManagerFactory entityManagerFactory;
    private Class<?>[] loadEntities;
    Integer flushWindowSize;
    boolean useMerge;

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
            SingleJsonDatasetLoaderBuilder builder = SingleJsonDatasetLoader.builder();
            this.datasetLoader = builder.datasetResource(testData.resource()).build();
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

    private void before(Description description) {
        this.dataAggregator = this.datasetLoader.load();
        if (this.entityManagerFactory != null) {
            this.populateTestDatabase();
        }
    }

    private void after(Description description) {
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

    private void populateTestDatabase() {
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
