package edu.ut.softlab.rate.component;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.IUserDao;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.IRateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    @Resource(name = "currencyDao")
    private ICurrencyDao currencyDao;

    @Resource(name = "serverConfig")
    private ServerConfig serverConfig;


    public void createCurrency(String code) {
        Currency currency = new Currency();
        currency.setCode(code);
        if (currencyDao.queryList("code", code.toString()).size() == 0) {
            dao.create(currency);
            System.out.println(code + " created");
        }
    }


    @Transactional
    public String addCurrencyAndRate(String code) {
        String resultMessage = "";
        Currency newCurrency = new Currency();
        newCurrency.setCode(code);
        if (currencyDao.queryList("code", code).size() == 0) {
            newCurrency.setRevision(currencyDao.getCurrentRev() + 1);
            dao.create(newCurrency);
            System.out.println(code + " created " + newCurrency.getCid());


            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            Calendar cl = Calendar.getInstance();
            cl.setTimeZone(TimeZone.getTimeZone("UTC"));
            cl.set(2013, Calendar.JANUARY, 1);
            cl.setTimeInMillis(Utility.getZeroTime(cl.getTime()));
            Date latestUpdate = cl.getTime();
            if (currencyDao.queryList("code", code).size() > 0) {
                if (!code.equals("USD")) {
                    ArrayList<String> result = Utility.postData(latestUpdate, code);
                    if (result.size() > 20) {
                        System.out.println(result.size());
                        Currency currency = dao.queryList("code", code).get(0);
                        double preRate = 0;
                        long preDate = latestUpdate.getTime();

                        for (int i = 2; i < result.size() - 1; i++) {
                            Rate rate = new Rate();
                            rate.setCurrency(currency);
                            String[] data = result.get(i).split(" ");

                            try {
                                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(data[1]);
                                long days = (date.getTime() - preDate) / (24 * 60 * 60 * 1000);
                                if (days > 1) {
                                    for (int j = 0; j < days - 1; j++) {
                                        //插入空缺的
                                        Rate voidRate = new Rate();
                                        voidRate.setCurrency(currency);
                                        voidRate.setValue(preRate);
                                        cl.setTimeInMillis(preDate);
                                        cl.add(Calendar.DATE, j + 1);
                                        voidRate.setDate(cl.getTimeInMillis());
                                        rateDao.create(voidRate);
                                    }
                                }
                                rate.setDate(date.getTime());
                                rate.setValue(Double.parseDouble(data[3]));
                                rateDao.create(rate);
                                preDate = rate.getDate();
                                preRate = rate.getValue();
                            } catch (Exception ex) {
                                System.out.println("date error " + code);
                                resultMessage = "date error " + code;
                            }
                        }


                        SimpleDateFormat yahooSf = new SimpleDateFormat("yyyyMMdd");
                        cl.setTimeInMillis(preDate);
                        System.out.println(cl.get(Calendar.DATE));
                        Calendar current = Calendar.getInstance();

                        if (current.get(Calendar.DATE) - cl.get(Calendar.DATE) >= 1) {
                            cl.add(Calendar.DATE, 1);
                            while (cl.get(Calendar.DATE) <= current.get(Calendar.DATE)) {
                                Map<String, Double> historyRate = Utility.getHistoryRateFromYahoo(yahooSf.format(cl.getTime()));
                                if (historyRate != null) {
                                    String currencyCodeStr = currency.getCode();
                                    if (historyRate.containsKey(currencyCodeStr + "=X")) {
                                        double rateValue = historyRate.get(currencyCodeStr + "=X");
                                        Rate lostRate = new Rate();
                                        lostRate.setCurrency(currency);
                                        lostRate.setValue(rateValue);
                                        lostRate.setDate(cl.getTimeInMillis());
                                        rateService.create(lostRate);
                                    } else {
                                        currencyDao.delete(newCurrency);
                                        System.out.println(currencyCodeStr + " doesn't include");
                                        resultMessage = currencyCodeStr + " doesn't include will be deleted";
                                        return resultMessage;
                                    }
                                } else {
                                    Map<String, Double> todayRate = Utility.getRateData();
                                    double rateValue = todayRate.get("USD/" + currency.getCode());
                                    Rate lostRate = new Rate();
                                    lostRate.setValue(rateValue);
                                    lostRate.setDate(cl.getTimeInMillis());
                                    lostRate.setCurrency(currency);
                                    rateService.create(lostRate);
                                    System.out.println(lostRate.getDate());
                                    resultMessage = lostRate.getDate().toString();
                                }
                                cl.add(Calendar.DATE, 1);
                            }
                        }
                        try {
                            File supplement = new File("src/rate_supplement.properties");
                            FileWriter fileWriter = new FileWriter(supplement, true);
                            BufferedWriter writer = new BufferedWriter(fileWriter);
                            writer.write(code + " = " + code + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }

                    } else {
                        currencyDao.delete(newCurrency);
                        System.out.println("error" + newCurrency.getCode() + " is deleted");
                        resultMessage = "error" + newCurrency.getCode() + " is deleted";
                    }
                }
            }
        } else {
            System.out.println(code + " is existing");
            resultMessage = code + " is existing";
        }
        return resultMessage;
    }


    @Scheduled(cron = "30 * * * * ? ") //30秒的时候更新
    @Transactional
    public void updateRate() {
        updateOrCreateCurrentRate(rateService);
    }

    /**
     * 通过雅虎api更新或者创建新的Rate记录
     *
     * @param rateService rateservice
     */
    public void updateOrCreateCurrentRate(IRateService rateService) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Map<String, Double> todayRate = Utility.getRateData();
        List<Rate> latestRates = rateService.getLatestRates();
        Date date = new Date();
        long currentMilliSeconds = Utility.getZeroTime(date);
        long lastMilliSeconds = latestRates.get(0).getDate();
        if (currentMilliSeconds == lastMilliSeconds) {
            System.out.println("yahoo update");
            for (Rate rate : latestRates) {
                rate.setValue(todayRate.get("USD/" + rate.getCurrency().getCode()));
                rateDao.update(rate);
            }
        } else {
            SimpleDateFormat yahooSf = new SimpleDateFormat("yyyyMMdd");
            Calendar cl = Calendar.getInstance();
            cl.setTimeZone(TimeZone.getTimeZone("UTC"));
            cl.setTimeInMillis(lastMilliSeconds);

            if ((date.getTime() - cl.getTimeInMillis()) / (24 * 60 * 60 * 1000) > 1) {
                cl.add(Calendar.DATE, 1);
                while (!yahooSf.format(date).equals(yahooSf.format(cl.getTime()))) {
                    Map<String, Double> historyRate = Utility.getHistoryRateFromYahoo(yahooSf.format(cl.getTime()));
                    if (historyRate != null) {
                        for (Rate rate : latestRates) {
                            String currencyCodeStr = rate.getCurrency().getCode();
                            if (!currencyCodeStr.equals("USD")) {
                                Currency currency = dao.queryList("code", currencyCodeStr).get(0);
                                System.out.println(currencyCodeStr + "=X");
                                if (historyRate.containsKey(currencyCodeStr + "=X")) {
                                    double rateValue = historyRate.get(currencyCodeStr + "=X");
                                    Rate lostRate = new Rate();
                                    lostRate.setCurrency(currency);
                                    lostRate.setValue(rateValue);
                                    lostRate.setDate(cl.getTimeInMillis());
                                    rateService.create(lostRate);
                                } else {
                                    System.out.println(currencyCodeStr + "doesn't include");
                                }
                            }
                        }
                        System.out.println(cl.getTime());
                    }
                    cl.add(Calendar.DATE, 1);
                    System.out.println("void");
                }
            }

            for (Rate rate : latestRates) {
                String currencyCodeStr = rate.getCurrency().getCode();
                if (!currencyCodeStr.equals("USD")) {
                    Currency currency = dao.queryList("code", currencyCodeStr).get(0);
                    if (todayRate.containsKey("USD/" + currencyCodeStr)) {
                        double rateValue = todayRate.get("USD/" + currencyCodeStr);
                        Rate currentRate = new Rate();
                        currentRate.setCurrency(currency);
                        currentRate.setValue(rateValue);
                        currentRate.setDate(Utility.getZeroTime(new Date()));
                        rateService.create(currentRate);
                    } else {
                        System.out.println(currencyCodeStr);
                    }
                }
            }
        }
    }

    @Scheduled(cron = "30 * * * * ? ") //每天十二点更新
    @Transactional
    public void notifyEmail() {
        try {
            org.springframework.core.io.Resource resource = new ClassPathResource("aps_development.p12");
            String certificate = resource.getFile().getPath();
            List<User> users = userDao.findAll();
            for (User user : users) {
                Set<Subscribe> subscribes = user.getSubscribes();
                StringBuilder sb = new StringBuilder();
                sb.append("This is a notification for your rate alert. The following subscribes hit the threshold you set before\n");

                for (Subscribe subscribe : subscribes) {
                    if (subscribe.getIsEnable()) {
                        double currentValue = rateService.getCurrentRate(subscribe.getCurrency().getCid(),
                                subscribe.getToCurrency().getCid());

                        if (subscribe.getMax() != 0 && subscribe.getMax() < currentValue) {
                            sb.append("Subscribe Name: " + subscribe.getSname() + " from currency: " + subscribe.getCurrency().getCode() + " to currency: " + subscribe.getToCurrency().getCode()
                                    + "the rate now (" + currentValue + ") is more than" + subscribe.getMax());
                            subscribe.setIsEnable(false);
                            if(subscribe.getIsSendEmail()){
                                Notification notification = new Notification(subscribe.getUser().getEmail(), "Rate Alert", sb.toString());
                                Thread notificationMailThread = new Thread(notification);
                                notificationMailThread.start();
                            }
                            for (Device device : user.getDevices()) {
                                if(device.getIsNotify()){
                                    String token = device.getDeviceToken();
                                    IPush iPush = new IPush(sb.toString(), token, certificate);
                                    Thread pushThread = new Thread(iPush);
                                    pushThread.start();
                                }
                            }
                        } else if (subscribe.getMin() != 0 && subscribe.getMin() > currentValue) {
                            sb.append("Subscribe Name: " + subscribe.getSname() + " from currency: " + subscribe.getCurrency().getCode() + " to currency: " + subscribe.getToCurrency().getCode()
                                    + "the rate now (" + currentValue + ") is lower than " + subscribe.getMin());
                            subscribe.setIsEnable(false);
                            if(subscribe.getIsSendEmail()){
                                Notification notification = new Notification(subscribe.getUser().getEmail(), "Rate Alert", sb.toString());
                                Thread notificationMailThread = new Thread(notification);
                                notificationMailThread.start();
                            }
                            for (Device device : user.getDevices()) {
                                if(device.getIsNotify()){
                                    String token = device.getDeviceToken();
                                    IPush iPush = new IPush(sb.toString(), token, certificate);
                                    Thread pushThread = new Thread(iPush);
                                    pushThread.start();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    private class Notification implements Runnable {
        private String email;
        private String content;
        private String subject;

        public Notification(String email, String subject, String content) {
            this.email = email;
            this.content = content;
            this.subject = subject;

        }

        @Override
        public void run() {
            Utility.send(email, subject, content);
        }
    }

    private class IPush implements Runnable {
        private String content;
        private String token;
        private String certificate;

        public IPush(String content, String token, String certificate) {
            this.content = content;
            this.token = token;
            this.certificate = certificate;
        }

        @Override
        public void run() {
            Utility.iphonePush(content, token, certificate);
        }
    }
}
