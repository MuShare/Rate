package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.RemoteMethod;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-11.
 */
@Repository("rateDao")
public class RateDao extends AbstractHibernateDao<Rate> implements IRateDao{
    public RateDao(){
        super();
        setClass(Rate.class);
    }


    @Override
    public List<Rate> getLatestRates() {
        DetachedCriteria maxDate = DetachedCriteria.forClass(Rate.class)
                .setProjection(Projections.max("date"));
        Criteria criteria = getCurrentSesstion().createCriteria(Rate.class)
                .add(Property.forName("date").eq(maxDate));
        return criteria.list();
    }

    @Override
    public Rate getLatestCurrencyRate(Currency currency) {
        System.out.println(currency.getCode());
        DetachedCriteria maxDate = DetachedCriteria.forClass(Rate.class)
                .setProjection(Projections.max("date"));
        Criteria criteria = getCurrentSesstion().createCriteria(Rate.class);
        criteria.add(Restrictions.eq("currency", currency))
                .add(Property.forName("date").eq(maxDate));
        System.out.println(criteria.list().size());
        return (Rate)criteria.uniqueResult();
    }




    @Override
    public List<Rate> getSpecificRateList(long startDate, long endDate, Currency currency) {
        Criteria crit = getCurrentSesstion().createCriteria(Rate.class)
                .add(Restrictions.eq("currency", currency))
                .add(Restrictions.between("date", startDate, endDate))
                .addOrder(Order.asc("date"));
        return crit.list();
    }
}
