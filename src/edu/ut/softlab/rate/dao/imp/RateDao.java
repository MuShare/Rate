package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.RemoteMethod;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-11.
 */
@Repository("rateDao")
public class RateDao extends AbstractHibernateDao<Rate> implements IRateDao{
    public RateDao(){
        super();
        setClass(Rate.class);
    }


    @Override
    public List<Rate> getLatestUpdateEntity() {
        String dateHQL = "select max(date) from Rate";
        List dateList = getCurrentSesstion().createQuery(dateHQL).list();
        Date date = (Date)dateList.get(0);
        String hql = "from Rate where date = :date";
        if(date != null){
            List<Rate> result = getCurrentSesstion().createQuery(hql).setString("date", date.toString()).list();
            return result;
        }else {
            return null;
        }
    }

    @Override
    public ChartData getChartData(String start, String endDate, Currency inCurrency, Currency outCurrency) {

        String hql = "from Rate where date >= :start and date <= :endDate and cid = :cid";

        List<Rate> inCurrencyList = getCurrentSesstion().createQuery(hql).setString("start", start)
                .setString("endDate", endDate)
                .setString("cid", outCurrency.getCid())
                .list();
        List<Rate> outCurrencyList = getCurrentSesstion().createQuery(hql).setString("start", start)
                .setString("endDate", endDate)
                .setString("cid", inCurrency.getCid())
                .list();
        ChartData chartData = new ChartData();
        chartData.setInCurrency(inCurrency.getCode());
        chartData.setOutCurrency(outCurrency.getCode());
        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            chartData.setTime(startDate.getTime()+32400000);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }

        if(!inCurrency.getCode().equals("USD") && !outCurrency.getCode().equals("USD")){
            for(int i=0; i<inCurrencyList.size(); i++){
                chartData.getData().add(Utility.round(outCurrencyList.get(i).getValue() / inCurrencyList.get(i).getValue(), 5));
            }
            return chartData;
        }else if(!inCurrency.getCode().equals("USD") && outCurrency.getCode().equals("USD")){
            for(int i=0; i<inCurrencyList.size(); i++){
                chartData.getData().add(Utility.round(1/inCurrencyList.get(i).getValue(), 5));
            }
            return chartData;
        }else if(inCurrency.getCode().equals("USD") && !outCurrency.getCode().equals("USD")){
            for(int i=0; i<outCurrencyList.size(); i++){
                chartData.getData().add(outCurrencyList.get(i).getValue());
            }
            return chartData;
        }else{
            return null;
        }
    }

    @Override
    public Rate getLatestCurrencyRate(Currency currency) {
        List<Rate> currentRates = getLatestUpdateEntity();
        for(Rate currentRate : currentRates){
            if(currentRate.getCurrency().getCid().equals(currency.getCid())){
                return currentRate;
            }
        }
        return null;
    }

    @Override
    public ChartData getSpecificRate(String start, String end, Currency currency) {
        String hql = "from Rate where date >= :start and date <= :endDate and cid = :cid";
        List<Rate> currencyRateList = getCurrentSesstion().createQuery(hql).setString("start", start)
                .setString("endDate", end)
                .setString("cid", currency.getCid())
                .list();
        ChartData chartData = new ChartData();
        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            chartData.setTime(startDate.getTime()+32400000);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        chartData.setInCurrency(currency.getCode());
        for(int i=0; i<currencyRateList.size(); i++){
            chartData.getData().add(currencyRateList.get(i).getValue());
        }
        return chartData;
    }
}
