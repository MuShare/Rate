package edu.ut.softlab.rate.controller;


import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.bean.RateResultBean;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.IRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-17.
 */

@Controller
@RequestMapping("/web/rate")
public class RateController {
    @Resource(name="rateService")
    private IRateService rateService;

    @Resource(name="currencyService")
    private ICurrencyService currencyService;

    @Resource(name = "deviceService")
    private IDeviceService deviceService;

    @Transactional
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getHistoryRate(@RequestParam(value = "from", required = false) String fromCid,
                                                              @RequestParam(value = "to",required = false) String toCid,
                                                              @RequestParam(value = "favorite", required = false, defaultValue = "false") Boolean fav,
                                                              HttpServletRequest request
                                                              ){
        Map<String, Object> result = new HashMap<>();
        if(fav){
            String token = request.getHeader("token");
           if(token == null){
               result.put(ResponseField.error_message, "token is required");
               result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
               return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
           }else{
               User user = deviceService.findUserByToken(token);
               if(user == null){
                   result.put(ResponseField.error_message, "token error");
                   result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
                   return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
               }else {
                   Set<Favorite> favorites = user.getFavorites();
                   String baseCid = fromCid != null ? fromCid : toCid;
                   edu.ut.softlab.rate.model.Currency baseCurrency = currencyService.findOne(baseCid);
                   double currentBaseRate = rateService.getCurrentRate(baseCurrency);

                   //List<RateResultBean> values = new ArrayList<>();
                   HashMap<String, Double> values = new HashMap<>();
                   for(Favorite favorite : favorites){
                       if(!favorite.getCurrency().getCid().equals(baseCid)){
                           Currency toCurrency = favorite.getCurrency();
                           double currentToRate = rateService.getCurrentRate(toCurrency);
                           if(fromCid != null && toCid == null){
                               values.put(toCurrency.getCid(), Utility.round(currentToRate / currentBaseRate, 5));
                           }else if (fromCid == null & toCid != null) {
                               values.put(toCurrency.getCid(), Utility.round(currentBaseRate / currentToRate, 5));
                           }
                       }
                   }

                   Map<String, Object> rates = new HashMap<>();
                   rates.put("rates", values);
                   result.put(ResponseField.result, rates);
                   result.put(ResponseField.HttpStatus, HttpStatus.OK.value());
                   return new ResponseEntity<>(result, HttpStatus.OK);
               }
           }
        }else if(fromCid == null || toCid == null){
            String baseCid = fromCid != null ? fromCid : toCid;
            if(baseCid == null){
                result.put(ResponseField.error_code, 344);
                result.put(ResponseField.error_message, "fromCid and toCid should not be null at the same time");
                result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            Currency baseCurrency = currencyService.findOne(baseCid);
            double currentBaseRate = rateService.getCurrentRate(baseCurrency);
            List<Currency> currencyList = currencyService.getCurrencyList();
            HashMap<String, Double> values = new HashMap<>();
            for(Currency currency:currencyList){
                if(!currency.getCid().equals(baseCurrency.getCid())){
                    double currentToRate = rateService.getCurrentRate(currency);
                    if(fromCid != null){
                        values.put(currency.getCid(), Utility.round(currentToRate / currentBaseRate, 5));
                    }else{
                        values.put(currency.getCid(), Utility.round(currentBaseRate / currentToRate, 5));
                    }
                }
            }
            Map<String, Object> rates = new HashMap<>();
            rates.put("rates", values);
            result.put(ResponseField.result, rates);
            result.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            Currency fromCurrency = currencyService.findOne(fromCid);
            Currency toCurrency = currencyService.findOne(toCid);
            double currentFromRate = rateService.getCurrentRate(fromCurrency);
            double currentToRate = rateService.getCurrentRate(toCurrency);

            double currentRate = Utility.round(currentToRate / currentFromRate, 5);
            Map<String, Object> value = new HashMap<>();
            value.put("rate", currentRate);
            result.put(ResponseField.result, value);
            result.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getCurrentRate(@RequestParam(value = "start", required = false) Long startTime,
                                                              @RequestParam(value = "end", required = false) Long endTime,
                                                              @RequestParam(value = "from", required = false) String fromCid,
                                                              @RequestParam(value = "to", required = false) String toCid){
        Map<String, Object> result = new HashMap<>();
        if(startTime == null || endTime == null){
            result.put(ResponseField.error_message, "startTime and endTime are required");
            result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        if(fromCid == null || toCid == null){
            result.put(ResponseField.error_message, "fromCid and toCid are required");
            result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        ChartData chartData = rateService.getHistoryRate(startTime, endTime, fromCid, toCid);
        result.put(ResponseField.result, chartData);
        result.put(ResponseField.HttpStatus, HttpStatus.OK.value());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}























