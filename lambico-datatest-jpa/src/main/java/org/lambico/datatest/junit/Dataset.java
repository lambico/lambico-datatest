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
    private Dataset(DatasetLoader datasetLoader, EntityManagerFactory entityManagerFactory, Class<?>[] loadEntities, Integer flushWindowSize, boolean useMerge) {
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
        return statement(base, description);
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
