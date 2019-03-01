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
public class FilmActorPK implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "actor_id")
    private short actorId;
    @Basic(optional = false)
    @Column(name = "film_id")
    private short filmId;

    public FilmActorPK() {
    }

    public FilmActorPK(short actorId, short filmId) {
        this.actorId = actorId;
        this.filmId = filmId;
    }

    public short getActorId() {
        return actorId;
    }

    public void setActorId(short actorId) {
        this.actorId = actorId;
    }

    public short getFilmId() {
        return filmId;
    }

    public void setFilmId(short filmId) {
        this.filmId = filmId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) actorId;
        hash += (int) filmId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof FilmActorPK)) {
            return false;
        }
        FilmActorPK other = (FilmActorPK) object;
        if (this.actorId != other.actorId) {
            return false;
        }
        if (this.filmId != other.filmId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.lambico.datatest.sakila.model.FilmActorPK[ actorId=" + actorId + ", filmId=" + filmId + " ]";
    }
    
}
