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

/**
 * Created by alex on 16-4-17.
 */
@Service("rateService")
@RemoteProxy
public class RateService extends AbstractService<Rate> implements IRateService {
    @Resource(name="rateDao")
    private IRateDao dao;

    @Resource(name="currencyDao")
    private ICurrencyDao currencyDao;

    @Override
    protected IOperations<Rate> getDao() {
        return this.dao;
    }


    @Override
    @RemoteMethod
    public ChartData getHistoryRate(String start, String end, String inCurrencyId, String outCurrencyId) {
        Currency inCurrency = currencyDao.findOne(inCurrencyId);
        Currency outCurrency = currencyDao.findOne(outCurrencyId);
        return this.dao.getChartData(start, end, inCurrency, outCurrency);
    }

    @Override
    @RemoteMethod
    public double getCurrentRate(String fromCurrencyCid, String toCurrencyCid) {
        Currency toCurrency = currencyDao.findOne(fromCurrencyCid);
        Currency fromCurrency = currencyDao.findOne(toCurrencyCid);
        if(fromCurrency != null && toCurrency != null){
            Rate fromRate = dao.getLatestCurrencyRate(fromCurrency);
            Rate toRate = dao.getLatestCurrencyRate(toCurrency);
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
        return dao.getSpecificRate(start, end,currency);
    }
}

