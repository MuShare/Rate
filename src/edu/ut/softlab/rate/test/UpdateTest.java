package edu.ut.softlab.rate.test;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.component.UpdateData;
import edu.ut.softlab.rate.controller.RateController;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IRateService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;


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
        Currency currency = currencyService.findOne("ff8081815688259c01568825ae7f0000");
        Currency currency1 = currencyService.findOne("ff808181568827c601568827d7450000");
        ChartData result = rateService.getHistoryRate(1438354800000L, 1469977200000L, "ff808181568827c601568827d7450000", "ff8081815688259c01568825ae7f0000");
        System.out.println(result.getData());
    }
}
