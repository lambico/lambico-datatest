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

@Entity
@Data
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Entity1 {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String stringField;
    @ManyToOne(cascade = CascadeType.ALL)
    private Entity2 entity2;

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
 
        if (!(o instanceof Entity1))
            return false;
 
        Entity1 other = (Entity1) o;
 
        return id != null &&
               id.equals(other.getId());
    }

}
