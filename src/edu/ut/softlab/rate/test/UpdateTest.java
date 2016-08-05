package edu.ut.softlab.rate.test;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.component.UpdateData;
import edu.ut.softlab.rate.controller.RateController;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IRateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * Created by alex on 16-4-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/mvc-dispatcher-servlet.xml", "classpath:/spring-hibernate.xml"})
@Transactional
public class UpdateTest {
    @Autowired
    UpdateData updateData;

    @Autowired
    RateController controller;

    @Autowired
    IRateService rateService;

    @Autowired
    ICurrencyDao currencyDao;

    @Autowired
    IRateDao rateDao;

    @Autowired
    ICurrencyService currencyService;


    @Value("#{supplement}")
    private Properties supplement;


    @Test
    @Rollback(false)
    @Transactional
    public void rateTest(){
        Currency currency = currencyService.findOne("ff808181564e243801564e2443950005");
        for(int i = 0; i < 500; i++){
            double rate = rateService.getCurrentRate(currency);
            System.out.println(rate);
        }
    }
}
