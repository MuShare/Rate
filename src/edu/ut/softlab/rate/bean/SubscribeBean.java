package edu.ut.softlab.rate.bean;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Subscribe;
import org.directwebremoting.annotations.DataTransferObject;

/**
 * Created by alex on 16-5-6.
 */
@DataTransferObject
public class SubscribeBean {
    private String sid;
    private String sname;
    private CurrencyBean fromCurrency;
    private CurrencyBean toCurrency;
    private UserBean userBean;
    private double min;
    private double max;
    private boolean isEnable;
    private boolean isOnce;
    private boolean isSendEmail;
    private boolean isSendSms;
    private double current;

    public SubscribeBean(String sid, String sname, CurrencyBean fromCurrency, CurrencyBean toCurrency, UserBean userBean,
                         double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms, double current){
        this.sid = sid;
        this.sname = sname;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.userBean = userBean;
        this.min = min;
        this.max = max;
        this.isEnable = isEnable;
        this.isOnce = isOnce;
        this.isSendEmail = isSendEmail;
        this.isSendSms = isSendSms;
        this.current = current;
    }

    public SubscribeBean(Subscribe subscribe){
        Currency fromCurrency = subscribe.getCurrency();
        Currency toCurrency = subscribe.getToCurrency();
        this.sid = subscribe.getSid();
        this.sname = subscribe.getSname();
        this.fromCurrency = new CurrencyBean(fromCurrency);
        this.toCurrency = new CurrencyBean(toCurrency);
        this.userBean = new UserBean(subscribe.getUser());
        this.min = subscribe.getMin();
        this.max = subscribe.getMax();
        this.isEnable = subscribe.getIsEnable();
        this.isOnce = subscribe.getIsOnce();
        this.isSendEmail = subscribe.getIsSendEmail();
        this.isSendSms = subscribe.getIsSendSms();
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public CurrencyBean getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(CurrencyBean fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public CurrencyBean getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CurrencyBean toCurrency) {
        this.toCurrency = toCurrency;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public boolean isOnce() {
        return isOnce;
    }

    public void setIsOnce(boolean isOnce) {
        this.isOnce = isOnce;
    }

    public boolean isSendEmail() {
        return isSendEmail;
    }

    public void setIsSendEmail(boolean isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    public boolean isSendSms() {
        return isSendSms;
    }

    public void setIsSendSms(boolean isSendSms) {
        this.isSendSms = isSendSms;
    }
}
