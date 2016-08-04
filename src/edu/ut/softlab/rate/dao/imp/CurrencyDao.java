package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Currency;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by alex on 16-4-11.
 */
@Repository("currencyDao")
public class CurrencyDao extends AbstractHibernateDao<Currency> implements ICurrencyDao {
    public CurrencyDao(){
        super();
        setClass(Currency.class);
    }

    @Override
    public int getCurrentRev() {
        Criteria criteria = getCurrentSesstion().createCriteria(Currency.class)
                .setProjection(Projections.max("revision"));
        return (int)criteria.uniqueResult();
    }

    @Override
    public List<Currency> getUpdatedCurrencies(int rev) {
        Criteria criteria = getCurrentSesstion().createCriteria((Currency.class))
                .add(Restrictions.gt("revision", rev));
        return criteria.list();
    }
}
