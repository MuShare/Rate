package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Currency;
import org.springframework.stereotype.Repository;

/**
 * Created by alex on 16-4-11.
 */
@Repository("currencyDao")
public class CurrencyDao extends AbstractHibernateDao<Currency> implements ICurrencyDao {
    public CurrencyDao(){
        super();
        setClass(Currency.class);
    }
}
