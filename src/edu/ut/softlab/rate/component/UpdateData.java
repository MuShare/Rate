package edu.ut.softlab.rate.component;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.IUserDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.service.IRateService;
import edu.ut.softlab.rate.service.imp.RateService;
import org.springframework.beans.factory.annotation.Value;
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

    @Resource(name = "rateService")
    private IRateService rateService;

    @Value("#{supplement}")
    private Properties supplement;

    public void createCurrency() {
        for (Object currencyCode : supplement.values()) {
            Currency currency = new Currency();
            currency.setCode(currencyCode.toString());
            dao.create(currency);
            if (dao != null) {
                System.out.println("not null");
            }
        }
    }

    @Scheduled(cron = "0 */1 * * * ?") //每分钟更新
    @Transactional
    public void updateRate() {
        List<Rate> latestRates = rateService.getLatestRates();
        Date latestUpdate;
        Calendar cl = Calendar.getInstance();

        if (latestRates.size() == 0) {
            latestUpdate = new GregorianCalendar(2013, 0, 0).getTime();
            for (Object currencyCode : supplement.values()) {
                String currencyCodeStr = currencyCode.toString();
                if (!currencyCodeStr.equals("USD")) {
                    ArrayList<String> result = Utility.postData(latestUpdate, currencyCodeStr);
                    System.out.println(result.size());
                    Currency currency = dao.queryList("code", currencyCodeStr).get(0);
                    double preRate = 0;
                    Date preDate = latestUpdate;

                    for (int i = 2; i < result.size() - 1; i++) {
                        if(currencyCodeStr.equals("CNY")){
                            System.out.println(result.get(i));
                        }
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
                                }
                            }
                            rate.setDate(date);
                            rate.setValue(Double.parseDouble(data[3]));
                            rateDao.create(rate);
                            preDate = rate.getDate();
                            preRate = rate.getValue();
                        } catch (Exception ex) {
                            System.out.println("date error "+ currencyCodeStr);
                        }
                    }
                }
            }
            //马上更新最新的汇率，如果之前没有获取到当天，则创建今天的Rate记录
            updateOrCreateCurrentRate(rateService);
        } else {
            updateOrCreateCurrentRate(rateService);
        }
    }

    /**
     * 通过雅虎api更新或者创建新的Rate记录
     * @param rateService rateservice
     */
    public void updateOrCreateCurrentRate(IRateService rateService){
        Map<String, Double> todayRate = Utility.getRateData();
        List<Rate> latestRates = rateService.getLatestRates();

        System.out.println(latestRates.get(0).getDate());
        System.out.println(new Date());

        if (latestRates.get(0).getDate().toString().
                equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {
            System.out.println("yahoo update");
            for (Rate rate : latestRates) {
                rate.setValue(todayRate.get("USD/" + rate.getCurrency().getCode()));
                rateDao.update(rate);
            }
        } else {
            for (Rate rate : latestRates) {
                String currencyCodeStr = rate.getCurrency().getCode();
                if (!currencyCodeStr.equals("USD")) {
                    Currency currency = dao.queryList("code", currencyCodeStr).get(0);
                    if(todayRate.containsKey("USD/" + currencyCodeStr)){
                        double rateValue = todayRate.get("USD/" + currencyCodeStr);
                        Rate currentRate = new Rate();
                        currentRate.setCurrency(currency);
                        currentRate.setValue(rateValue);
                        currentRate.setDate(new Date());
                        rateService.create(currentRate);
                    }else {
                        System.out.println(currencyCodeStr);
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
                double currentValue = rateService.getCurrentRate(subscribe.getCurrency().getCid(),
                        subscribe.getToCurrency().getCid());

                boolean inRange = (currentValue >= subscribe.getMin() && currentValue <= subscribe.getMax());
                if(inRange != subscribe.getInRange()){
                    Utility.send(subscribe.getUser().getEmail(), "Notify: ");
                }
            }
        }
    }
}
