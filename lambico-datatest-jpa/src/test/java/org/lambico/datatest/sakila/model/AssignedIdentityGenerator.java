package org.lambico.datatest.sakila.model;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

/**
 * A generator for using fixed values for ids.
 * 
 * Derived from https://github.com/vladmihalcea/high-performance-java-persistence/blob/master/core/src/test/java/com/vladmihalcea/book/hpjp/hibernate/identifier/AssignedIdentityGenerator.java
 */
public class AssignedIdentityGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        if(obj instanceof Address) {
            Address address = (Address) obj;
            Serializable id = address.getAddressId();
            if(id != null) {
                return id;
            }
        }
        if(obj instanceof Rental) {
            Rental rental = (Rental) obj;
            Serializable id = rental.getRentalId();
            if(id != null) {
                return id;
            }
        }
        return super.generate(session, obj);
    }
}