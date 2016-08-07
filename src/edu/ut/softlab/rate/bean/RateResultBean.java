package edu.ut.softlab.rate.bean;

/**
 * Created by alex on 16-8-4.
 */
public class RateResultBean {
    private String cid;
    private double value;
    private String code;

    public RateResultBean(String cid, String code, double value){
        this.cid = cid;
        this.value = value;
        this.code = code;
    }

    public String getCid() {
        return cid;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
