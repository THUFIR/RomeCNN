/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dur.bounceme.net.rome.jpa;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thufir
 */
@Entity
@Table(name = "feeds", catalog = "rome_aggregator", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"url"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feed.findAll", query = "SELECT f FROM Feed f"),
    @NamedQuery(name = "Feed.findById", query = "SELECT f FROM Feed f WHERE f.id = :id"),
    @NamedQuery(name = "Feed.findByCreated", query = "SELECT f FROM Feed f WHERE f.created = :created"),
    @NamedQuery(name = "Feed.findByUrl", query = "SELECT f FROM Feed f WHERE f.url = :url")})
public class Feed implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(name = "url", nullable = false, length = 767)
    private String url;

    public Feed() {
    }

    public Feed(Integer id) {
        this.id = id;
    }

    public Feed(Integer id, Date created, String url) {
        this.id = id;
        this.created = created;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feed)) {
            return false;
        }
        Feed other = (Feed) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dur.bounceme.net.rome.jpa.Feed[ id=" + id + " ]";
    }
    
}
