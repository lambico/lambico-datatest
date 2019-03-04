package org.lambico.datatest.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;
import org.lambico.datatest.jpa.JpaUtils;

/**
 * A generator for using fixed values for ids.
 * 
 * Inspired by
 * https://github.com/vladmihalcea/high-performance-java-persistence/blob/master/core/src/test/java/com/vladmihalcea/book/hpjp/hibernate/identifier/AssignedIdentityGenerator.java
 */
public class AssignedIdentityGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable id = JpaUtils.getIdValue(session.getFactory().getMetamodel(), obj);
        if (id != null) {
            return id;
        }
        return super.generate(session, obj);
    }
}
