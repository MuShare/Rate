package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 16-4-11.
 */
public interface IRateDao extends IOperations<Rate>{
    List<Rate> getLatestRates();
    Rate getLatestCurrencyRate(Currency currency);
    List<Rate> getSpecificRateList(long start, long end, Currency currency);
}
