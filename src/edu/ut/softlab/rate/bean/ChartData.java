package edu.ut.softlab.rate.bean;

import org.directwebremoting.annotations.DataTransferObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 16-4-17.
 */
@DataTransferObject
public class ChartData {
    private List<Double> data;
    private Long time;
    private String inCurrency;
    private String outCurrency;


    public ChartData(){
        this.data = new ArrayList<>();
    }

    public List<Double> getData() {
        return data;
    }

    public void setData(List<Double> data) {

        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getInCurrency() {
        return inCurrency;
    }

    public void setInCurrency(String inCurrency) {
        this.inCurrency = inCurrency;
    }

    public String getOutCurrency() {
        return outCurrency;
    }

    public void setOutCurrency(String outCurrency) {
        this.outCurrency = outCurrency;
    }
}
