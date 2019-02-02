package org.lambico.datatest.example1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Entity1 {
    @EqualsAndHashCode.Exclude
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String stringField;
    private Entity2 entity2;
}