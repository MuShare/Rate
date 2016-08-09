package edu.ut.softlab.rate.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.directwebremoting.annotations.DataTransferObject;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by alex on 16-4-11.
 */

@Entity(name = "Subscribe")
@Table(name="subscribe")
@DataTransferObject
public class Subscribe implements Serializable{
    private static final long serialVersionUID = 1L;
    public Subscribe(){super();}

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "sid", unique = true)
    private String sid;

    @ManyToOne
    @JoinColumn(name="fromcid")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name="tocid")
    private Currency toCurrency;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    @Column(name="sname", length = 45)
    private String sname;

    @Column(name="min")
    private Double min = 0.0;

    @Column(name="max")
    private Double max = 0.0;

    @Column(name="isenable")
    private Boolean isEnable = true;

    @Column(name="isOnce")
    private Boolean isOnce = true;

    @Column(name="isSendEmail")
    private Boolean isSendEmail = true;

    @Column(name="isSendSms")
    private Boolean isSendSms = true;

    @Column(name="revision")
    private Integer revision = 0;

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    private boolean inRange;

    public boolean getInRange() {
        return inRange;
    }

    public void setInRange(boolean inRange) {
        this.inRange = inRange;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public Boolean getIsOnce() {
        return isOnce;
    }

    public void setIsOnce(Boolean isOnce) {
        this.isOnce = isOnce;
    }

    public Boolean getIsSendEmail() {
        return isSendEmail;
    }

    public void setIsSendEmail(Boolean isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    public Boolean getIsSendSms() {
        return isSendSms;
    }

    public void setIsSendSms(Boolean isSendSms) {
        this.isSendSms = isSendSms;
    }

    public Currency getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(Currency toCurrency) {
        this.toCurrency = toCurrency;
    }
}
