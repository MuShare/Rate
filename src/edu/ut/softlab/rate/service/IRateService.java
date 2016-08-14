package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;

import java.util.Date;
import java.util.List;

/**
 * Created by alex on 16-4-17.
 */
public interface IRateService extends IOperations<Rate> {
    ChartData getHistoryRate(long start, long end, String inCurrency, String outCurrency);
    double getCurrentRate(String fromCurrency, String toCurrency);
    ChartData getSpecificRate(String start, String end, String currencyCid);
    ChartData getSpecificRate(long start, long end, Currency currency);
    double getCurrentRate(Currency currency);
    List<Rate> getLatestRates();
}
