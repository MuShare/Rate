package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Rate;

import java.util.Date;

/**
 * Created by alex on 16-4-17.
 */
public interface IRateService extends IOperations<Rate> {
    ChartData getHistoryRate(String start, String end, String inCurrency, String outCurrency);
    double getCurrentRate(String fromCurrency, String toCurrency);
    ChartData getSpecificRate(String start, String end, String currencyCid);
}
