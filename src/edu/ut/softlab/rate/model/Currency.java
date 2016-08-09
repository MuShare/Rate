package edu.ut.softlab.rate.model;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by alex on 16-4-11.
 */
@Entity(name = "Currency")
@Table(name = "currency")
@DataTransferObject
public class Currency implements Serializable {
    private static final long serialVersionUID = 1L;
    public Currency(){super();}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "cid", unique = true)
    private String cid;

    @Column(name = "code", length = 5)
    private String code;

    @Column(name = "revision")
    private Integer revision = 0;

    @OneToMany (mappedBy = "currency", cascade = CascadeType.ALL)
    private Set<Subscribe> fromCurrencySubscribes;

    @OneToMany (mappedBy = "toCurrency", cascade = CascadeType.ALL)
    private Set<Subscribe> toCurrencySubscribes;

    @OneToMany (mappedBy = "currency", cascade = CascadeType.ALL)
    private Set<Rate> rates;

    @OneToMany (mappedBy = "currency", cascade = CascadeType.ALL)
    private Set<Favorite> favorites;

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Set<Subscribe> getFromCurrencySubscribes() {
        return fromCurrencySubscribes;
    }

    public void setFromCurrencySubscribes(Set<Subscribe> fromCurrencySubscribes) {
        this.fromCurrencySubscribes = fromCurrencySubscribes;
    }

    public Set<Subscribe> getToCurrencySubscribes() {
        return toCurrencySubscribes;
    }

    public void setToCurrencySubscribes(Set<Subscribe> toCurrencySubscribes) {
        this.toCurrencySubscribes = toCurrencySubscribes;
    }

    public Set<Rate> getRates() {
        return rates;
    }

    public void setRates(Set<Rate> rates) {
        this.rates = rates;
    }
}
