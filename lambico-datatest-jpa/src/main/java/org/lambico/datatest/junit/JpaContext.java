package org.lambico.datatest.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class JpaContext implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(JpaContext.class);
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public JpaContext(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
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

    private void before(Description description) {
        log.debug("Staring transaction with rollback only");
        this.entityManager = this.entityManagerFactory.createEntityManager();
        this.entityManager.getTransaction().begin();
        this.entityManager.getTransaction().setRollbackOnly();
    }

    private void after(Description description) {
        this.entityManager.getTransaction().rollback();
        this.entityManager.close();
        log.debug("Transaction rollbacked");
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
