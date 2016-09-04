package edu.ut.softlab.rate.webbean;

import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.DataTransferObject;

/**
 * Created by alex on 16-5-6.
 */
@DataTransferObject
public class WebCurrencyBean {
    private String cid;
    private String code;
    private String icon;
    private String name;
    private double latitude;
    private double longitude;

    public WebCurrencyBean(Currency currency){
        cid = currency.getCid();
        code = currency.getCode();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
