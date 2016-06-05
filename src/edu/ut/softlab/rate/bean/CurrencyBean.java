package edu.ut.softlab.rate.bean;

import org.directwebremoting.annotations.DataTransferObject;

/**
 * Created by alex on 16-5-6.
 */
@DataTransferObject
public class CurrencyBean {
    private String cid;
    private String code;

    public CurrencyBean(String cid, String code){
        this.cid = cid;
        this.code = code;
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
}
