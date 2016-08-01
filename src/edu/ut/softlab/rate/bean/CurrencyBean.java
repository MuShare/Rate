package edu.ut.softlab.rate.bean;

import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.DataTransferObject;

/**
 * Created by alex on 16-5-6.
 */
@DataTransferObject
public class CurrencyBean {
    private String cid;
    private String code;
    private String icon;

    public CurrencyBean(Currency currency){
        cid = currency.getCid();
        code = currency.getCode();
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
