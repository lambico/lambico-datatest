package org.lambico.datatest.sakila.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

/**
 *
 * @author lucio
 */
@Embeddable
@JsonIdentityInfo(generator=JSOGGenerator.class)
public class FilmCategoryPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "film_id")
    private short filmId;
    @Basic(optional = false)
    @Column(name = "category_id")
    private short categoryId;

    public FilmCategoryPK() {
    }

    public FilmCategoryPK(short filmId, short categoryId) {
        this.filmId = filmId;
        this.categoryId = categoryId;
    }

    public short getFilmId() {
        return filmId;
    }

    public void setFilmId(short filmId) {
        this.filmId = filmId;
    }

    public short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(short categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) filmId;
        hash += (int) categoryId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof FilmCategoryPK)) {
            return false;
        }
        FilmCategoryPK other = (FilmCategoryPK) object;
        if (this.filmId != other.filmId) {
            return false;
        }
        if (this.categoryId != other.categoryId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lambico.datatest.sakila.model.FilmCategoryPK[ filmId=" + filmId + ", categoryId=" + categoryId + " ]";
    }
    
}
