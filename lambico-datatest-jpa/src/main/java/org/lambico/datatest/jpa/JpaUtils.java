package org.lambico.datatest.jpa;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import javax.persistence.metamodel.Metamodel;

public final class JpaUtils {
    private JpaUtils() {
    }

    public static Serializable getIdValue(Metamodel metamodel, Object entity) {
        Member idMember = metamodel.entity(entity.getClass()).getId(Serializable.class).getJavaMember();
        Serializable id = null;
        if (idMember instanceof Field) {
            Field idField = (Field) idMember;
            idField.setAccessible(true);
            try {
                id = (Serializable) idField.get(entity);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new IllegalArgumentException("Error retriving primary key value", e);
            }
        }
        if (idMember instanceof Method) {
            Method method = (Method) idMember;
            method.setAccessible(true);
            try {
                id = (Serializable) method.invoke(entity, new Object[] {});
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalArgumentException("Error retriving primary key value", e);
            }
        }
        return id;
    }
}
