package edu.ut.softlab.rate.bean;

/**
 * Created by alex on 16-8-4.
 */
public class RateResultBean {
    private String cid;
    private double value;

    public RateResultBean(String cid, double value){
        this.cid = cid;
        this.value = value;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
