/**
 * Copyright © 2018 The Lambico Datatest Team (lucio.benfante@gmail.com)
 *
 * This file is part of lambico-datatest-jpa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lambico.datatest.sakila.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

/**
 *
 * @author lucio
 */
@Entity
@Table(name = "language")
@NamedQueries({
    @NamedQuery(name = "Language.findAll", query = "SELECT l FROM Language l")})
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "language_id")
    private Integer languageId;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "language")
    private Collection<Film> filmsInThisLanguage;
    @OneToMany(mappedBy = "originalLanguage")
    private Collection<Film> filmsWithThisOriginalLanguage;

    public Language() {
    }

    public Language(Integer languageId) {
        this.languageId = languageId;
    }

    public Language(Integer languageId, String name, Date lastUpdate) {
        this.languageId = languageId;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return Collection<Film> return the filmsInThisLanguage
     */
    public Collection<Film> getFilmsInThisLanguage() {
        return filmsInThisLanguage;
    }

    /**
     * @param filmsInThisLanguage the filmsInThisLanguage to set
     */
    public void setFilmsInThisLanguage(Collection<Film> filmsInThisLanguage) {
        this.filmsInThisLanguage = filmsInThisLanguage;
    }

    /**
     * @return Collection<Film> return the filmsWithThisOriginalLanguage
     */
    public Collection<Film> getFilmsWithThisOriginalLanguage() {
        return filmsWithThisOriginalLanguage;
    }

    /**
     * @param filmsWithThisOriginalLanguage the filmsWithThisOriginalLanguage to set
     */
    public void setFilmsWithThisOriginalLanguage(Collection<Film> filmsWithThisOriginalLanguage) {
        this.filmsWithThisOriginalLanguage = filmsWithThisOriginalLanguage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (languageId != null ? languageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Language)) {
            return false;
        }
        Language other = (Language) object;
        if ((this.languageId == null && other.languageId != null) || (this.languageId != null && !this.languageId.equals(other.languageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lambico.datatest.sakila.model.Language[ languageId=" + languageId + " ]";
    }

}
