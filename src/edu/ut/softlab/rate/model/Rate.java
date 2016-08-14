package edu.ut.softlab.rate.model;

import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alex on 16-4-11.
 */
@Entity(name = "Rate")
@Table(name = "rate")
@DataTransferObject
public class Rate implements Serializable {
    private static final long serialVersionUID = 1L;
    public Rate(){super();}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "rid", unique = true)
    private String rid;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name="value")
    private Double value;

    @ManyToOne
    @JoinColumn(name="cid")
    private Currency currency;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
