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
    public ChartData getHistoryRate(long start, long end, String inCurrencyId, String outCurrencyId) {
        ChartData chartData = new ChartData();
        Currency inCurrency = currencyDao.findOne(inCurrencyId);
        Currency outCurrency = currencyDao.findOne(outCurrencyId);

        List<Rate> inCurrencyRateList = rateDao.getSpecificRateList(start, end, inCurrency);
        List<Rate> outCurrencyRateList = rateDao.getSpecificRateList(start, end, outCurrency);

        chartData.setInCurrency(inCurrency.getCode());
        chartData.setOutCurrency(outCurrency.getCode());

        if(inCurrencyRateList.size() != 0){
            chartData.setTime(inCurrencyRateList.get(0).getDate());
        }else {
            chartData.setTime(outCurrencyRateList.get(0).getDate());
        }

        System.out.println(chartData.getTime());


        if(!inCurrency.getCode().equals("USD") && !outCurrency.getCode().equals("USD")){
            for(int i=0; i<inCurrencyRateList.size(); i++){
                chartData.getData().add(Utility.round(outCurrencyRateList.get(i).getValue() / inCurrencyRateList.get(i).getValue(), 5));
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
    public double getCurrentRate(Currency currency) {
        if(currency.getCode().equals("USD")){
            return 1.0;
        }else {
            Rate rate = rateDao.getLatestCurrencyRate(currency);
            return rate.getValue();
        }
    }

    @Override
    public List<Rate> getLatestRates() {
        return rateDao.getLatestRates();
    }
}

