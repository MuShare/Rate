package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;

import java.util.List;

/**
 * Created by alex on 16-4-11.
 */
public interface ICurrencyDao extends IOperations<Currency> {
    int getCurrentRev();
    List<Currency> getUpdatedCurrencies(int rev);
}
