package edu.ut.softlab.rate.component;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.IUserDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-12.
 */

@Component("updateData")
@Lazy(false)
public class UpdateData {
    @Resource(name = "currencyDao")
    private ICurrencyDao dao;

    @Resource(name = "rateDao")
    private IRateDao rateDao;

    @Resource(name = "userDao")
    private IUserDao userDao;

    public void createCurrency() {
        for (Utility.CurrencyCode currencyCode : Utility.CurrencyCode.values()) {
            Currency currency = new Currency();
            currency.setCode(currencyCode.name());
            dao.create(currency);
            if (dao != null) {
                System.out.println("not null");
            }
        }
    }

    @Scheduled(cron = "0 */1 * * * ?") //每天十二点更新
    @Transactional
    public void updateRate() {
        System.out.println("hello");
        List<Rate> latestData = rateDao.getLatestUpdateEntity();
        Date latestUpdate;
        Calendar cl = Calendar.getInstance();

        if (latestData == null) {
            latestUpdate = new GregorianCalendar(2013, 0, 0).getTime();
            for (Utility.CurrencyCode currencyCode : Utility.CurrencyCode.values()) {
                if (!currencyCode.name().equals("USD")) {
                    ArrayList<String> result = Utility.getUtility().postData(latestUpdate, currencyCode.name());
                    Currency currency = dao.queryList("code", currencyCode.name()).get(0);
                    double preRate = 0;
                    Date preDate = latestUpdate;

                    for (int i = 2; i < result.size() - 1; i++) {

                        Rate rate = new Rate();
                        rate.setCurrency(currency);
                        String[] data = result.get(i).split(" ");

                        try {
                            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(data[1]);
                            long days = (date.getTime() - preDate.getTime()) / (24 * 60 * 60 * 1000);
                            if (days > 1) {
                                for (int j = 0; j < days - 1; j++) {
                                    //插入空缺的
                                    Rate voidRate = new Rate();
                                    voidRate.setCurrency(currency);
                                    voidRate.setValue(preRate);
                                    cl.setTime(preDate);
                                    cl.add(Calendar.DATE, j + 1);
                                    voidRate.setDate(cl.getTime());
                                    rateDao.create(voidRate);
                                    System.out.println(voidRate.getDate());
                                }
                            }
                            rate.setDate(date);
                        } catch (Exception ex) {
                            System.out.println("date error");
                        }
                        rate.setValue(Double.parseDouble(data[3]));
                        rateDao.create(rate);
                        preDate = rate.getDate();
                        preRate = rate.getValue();
                    }
                }
            }
            //當天的無法獲取 所以再次調用一次雅虎的api
            Map<String, Double> todayRate = Utility.getUtility().getRateData();
            System.out.println(todayRate);
            for (Utility.CurrencyCode currencyCode : Utility.CurrencyCode.values()) {
                if (!currencyCode.name().equals("USD")) {
                    Currency currency = dao.queryList("code", currencyCode.name()).get(0);
                    double rateValue = todayRate.get("USD/" + currencyCode.name());
                    Rate rate = new Rate();
                    rate.setCurrency(currency);
                    rate.setValue(rateValue);
                    rate.setDate(new Date());
                    rateDao.create(rate);
                }
            }
        } else {
            Map<String, Double> todayRate = Utility.getUtility().getRateData();
            List<Rate> latestRate = rateDao.getLatestUpdateEntity();
            if (latestRate.get(0).getDate().toString().
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
                System.out.println("update");
                for (Rate rate : latestRate) {
                    rate.setValue(todayRate.get("USD/" + rate.getCurrency().getCode()));
                    rateDao.update(rate);
                }
            } else {
                for (Utility.CurrencyCode currencyCode : Utility.CurrencyCode.values()) {
                    if (!currencyCode.name().equals("USD")) {
                        Currency currency = dao.queryList("code", currencyCode.name()).get(0);
                        double rateValue = todayRate.get("USD/" + currencyCode.name());
                        Rate rate = new Rate();
                        rate.setCurrency(currency);
                        rate.setValue(rateValue);
                        rate.setDate(new Date());
                        rateDao.create(rate);
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 40 19 * * *") //每天十二点更新
    @Transactional
    public void notifyEmail() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            Set<Subscribe> subscribes = user.getSubscribes();
            StringBuilder sb = new StringBuilder();
            for (Subscribe subscribe : subscribes) {

            }
        }
    }
}
