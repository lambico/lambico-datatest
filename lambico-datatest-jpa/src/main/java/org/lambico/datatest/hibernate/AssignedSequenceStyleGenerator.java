package org.lambico.datatest.hibernate;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.lambico.datatest.jpa.JpaUtils;

/**
 * A generator for using fixed values for ids.
 * 
 * Inspired by https://github.com/vladmihalcea/high-performance-java-persistence/blob/master/core/src/test/java/com/vladmihalcea/book/hpjp/hibernate/identifier/AssignedIdentityGenerator.java
 */
public class AssignedSequenceStyleGenerator extends SequenceStyleGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable id = JpaUtils.getIdValue(session.getFactory().getMetamodel(), obj);
        if (id != null) {
            return id;
        }
        return super.generate(session, obj);
    }
}
