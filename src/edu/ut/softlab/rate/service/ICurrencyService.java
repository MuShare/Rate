package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;

import java.util.List;

/**
 * Created by alex on 16-5-3.
 */
public interface ICurrencyService  extends IOperations<Currency> {
    List<Currency> getCurrencyList();
    List<Currency> getUpdatedCurrencies(int rev);
    int getCurrentRevision();
    String addCurrency(String code);
}
