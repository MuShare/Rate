package edu.ut.softlab.rate.service.imp;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.service.IRateService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 16-4-17.
 */
@Service("rateService")
@RemoteProxy
public class RateService extends AbstractService<Rate> implements IRateService {
    @Resource(name="rateDao")
    private IRateDao rateDao;

    @Resource(name="currencyDao")
    private ICurrencyDao currencyDao;

    @Override
    protected IOperations<Rate> getDao() {

        return rateDao;
    }


    @Override
    @RemoteMethod
    public ChartData getHistoryRate(String start, String end, String inCurrencyId, String outCurrencyId) {
        ChartData chartData = new ChartData();
        Currency inCurrency = currencyDao.findOne(inCurrencyId);
        Currency outCurrency = currencyDao.findOne(outCurrencyId);
        Date startDate=null, endDate=null;
        try{
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            chartData.setTime(startDate.getTime());
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        List<Rate> inCurrencyRateList = rateDao.getSpecificRateList(startDate, endDate, inCurrency);
        List<Rate> outCurrencyRateList = rateDao.getSpecificRateList(startDate, endDate, outCurrency);

        chartData.setInCurrency(inCurrency.getCode());
        chartData.setOutCurrency(outCurrency.getCode());


        if(!inCurrency.getCode().equals("USD") && !outCurrency.getCode().equals("USD")){
            for(int i=0; i<inCurrencyRateList.size(); i++){
                chartData.getData().add(Utility.round(inCurrencyRateList.get(i).getValue() / outCurrencyRateList.get(i).getValue(), 5));
            }
            return chartData;
        }else if(!inCurrency.getCode().equals("USD") && outCurrency.getCode().equals("USD")){
            for(int i=0; i<inCurrencyRateList.size(); i++){
                chartData.getData().add(Utility.round(1/inCurrencyRateList.get(i).getValue(), 5));
            }
            return chartData;
        }else if(inCurrency.getCode().equals("USD") && !outCurrency.getCode().equals("USD")){
            for(int i=0; i<outCurrencyRateList.size(); i++){
                chartData.getData().add(outCurrencyRateList.get(i).getValue());
            }
            return chartData;
        }else{
            return null;
        }
    }


    @Override
    @RemoteMethod
    public double getCurrentRate(String fromCurrencyCid, String toCurrencyCid) {
        Currency toCurrency = currencyDao.findOne(fromCurrencyCid);
        Currency fromCurrency = currencyDao.findOne(toCurrencyCid);
        if(fromCurrency != null && toCurrency != null){
            Rate fromRate = rateDao.getLatestCurrencyRate(fromCurrency);
            Rate toRate = rateDao.getLatestCurrencyRate(toCurrency);
            if (fromRate != null && toRate != null){
                return Utility.round(fromRate.getValue()/toRate.getValue(),5);
            }else if(fromRate == null){
                return toRate.getValue();
            }else {
                return Utility.round(1/fromRate.getValue(), 5);
            }
        }
        return 0;
    }

    @Override
    @RemoteMethod
    public ChartData getSpecificRate(String start, String end, String currencyCid) {
        Currency currency = currencyDao.findOne(currencyCid);
        return rateDao.getSpecificRate(start, end,currency);
    }

    @Override
    public ChartData getSpecificRate(Date start, Date end, Currency currency){
        ChartData chartData = new ChartData();
        List<Rate> rateList = rateDao.getSpecificRateList(start, end, currency);
        List<Double> rateValues = new ArrayList<>();
        for(Rate rate : rateList){
            rateValues.add(rate.getValue());
        }
        chartData.setData(rateValues);
        chartData.setTime(start.getTime());
        return chartData;
    }
}

