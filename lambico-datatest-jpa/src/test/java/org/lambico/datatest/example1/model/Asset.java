package org.lambico.datatest.example1.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Asset {
    @EqualsAndHashCode.Exclude
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String assetName;
    @OneToMany(mappedBy = "asset")
    private List<Job> jobs;

}