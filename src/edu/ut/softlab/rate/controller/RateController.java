package edu.ut.softlab.rate.controller;


import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IRateService;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by alex on 16-4-17.
 */

@Controller
@RequestMapping("/rate")
public class RateController {
    @Resource(name="rateService")
    private IRateService rateService;

    @Resource(name="currencyService")
    private ICurrencyService currencyService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getRate(@RequestParam(value = "start", required = false) Long startTime,
                                                       @RequestParam(value = "end", required = false) Long endTime,
                                                       @RequestParam(value = "from", required = true) String fromCid,
                                                       @RequestParam(value = "to",required = false) String toCid
                                                       ){
        Map<String, Object> result = new HashMap<>();
        //如果为空 则为一年的间隔
        Date startDate = null;
        if(startTime == null){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(System.currentTimeMillis()));
            c.add(Calendar.YEAR, -1);
            startDate = c.getTime();
        }else {
            startDate = new Date(startTime);
        }
        Date endDate = endTime == null?new Date(System.currentTimeMillis()) : new Date(endTime);

        Currency from = currencyService.findOne(fromCid);
        if(toCid == null){
            if(!from.getCode().equals("USD")){
                System.out.println(startDate);
                ChartData chartData = rateService.getSpecificRate(startDate, endDate, from);
                result.put(ResponseField.result, chartData);
                result.put(ResponseField.HttpStatus, HttpStatus.OK);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }else {
                result.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST);
                result.put(ResponseField.error_message, "the first currency can not be USD");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
        }else {
            String startDateStr = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
            System.out.println(startDateStr);
            String endDateStr = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
            ChartData chartData = rateService.getHistoryRate(startDateStr, endDateStr, fromCid, toCid);
            result.put(ResponseField.result, chartData);
            result.put(ResponseField.HttpStatus, HttpStatus.OK);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
}
