package edu.ut.softlab.rate.bean;

import edu.ut.softlab.rate.model.Currency;

/**
 * Created by alex on 16-8-3.
 */
public class CurrencyResourceBean {
    private String cid;
    private String displayname;
    private String icon;

    public CurrencyResourceBean(Currency currency){
        this.cid = currency.getCid();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
