package org.lambico.datatest.example1.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Entity2 {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String stringField;
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude Entity1 entity1;

    /**
     * See https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     */
    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
 
        if (!(o instanceof Entity2))
            return false;
 
        Entity2 other =  (Entity2) o;
 
        return id != null &&
               id.equals(other.getId());
    }
}
