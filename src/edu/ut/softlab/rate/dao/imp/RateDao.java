package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import org.directwebremoting.annotations.RemoteMethod;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
    public List<Rate> getLatestUpdateEntity() {
        String dateHQL = "select max(date) from Rate";
        List dateList = getCurrentSesstion().createQuery(dateHQL).list();
        Date date = (Date)dateList.get(0);
        String hql = "from Rate where date = :date";
        if(date != null){
            List<Rate> result = getCurrentSesstion().createQuery(hql).setString("date", date.toString()).list();
            return result;
        }else {
            return null;
        }
    }

    @Override
    public Rate getLatestCurrencyRate(Currency currency) {
        List<Rate> currentRates = getLatestUpdateEntity();
        for(Rate currentRate : currentRates){
            if(currentRate.getCurrency().getCid().equals(currency.getCid())){
                return currentRate;
            }
        }
        return null;
    }

    @Override
    public ChartData getSpecificRate(String start, String end, Currency currency) {
        String hql = "from Rate where date >= :start and date <= :endDate and cid = :cid";
        List<Rate> currencyRateList = getCurrentSesstion().createQuery(hql).setString("start", start)
                .setString("endDate", end)
                .setString("cid", currency.getCid())
                .list();
        ChartData chartData = new ChartData();
        try{
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            chartData.setTime(startDate.getTime()+32400000);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
        chartData.setInCurrency(currency.getCode());
        for(int i=0; i<currencyRateList.size(); i++){
            chartData.getData().add(currencyRateList.get(i).getValue());
        }
        return chartData;
    }



    @Override
    public List<Rate> getSpecificRateList(Date startDate, Date endDate, Currency currency) {
        Criteria crit = getCurrentSesstion().createCriteria(Rate.class)
                .add(Restrictions.eq("currency", currency))
                .add(Restrictions.between("date", startDate, endDate))
                .addOrder(Order.asc("date"));
        return crit.list();
    }
}
