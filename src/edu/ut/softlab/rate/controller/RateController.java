package edu.ut.softlab.rate.controller;


import edu.ut.softlab.rate.bean.ChartData;
import edu.ut.softlab.rate.model.Rate;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 16-4-17.
 */

@Controller
@RequestMapping("/rate")
@RemoteProxy(name="test")
public class RateController {
    @Resource(name="rateService")
    private IRateService rateService;

//    @Transactional
//    @RequestMapping(value="/getrate", method= RequestMethod.POST)
//    public ResponseEntity<ChartData> getRate(){
//        List<Rate> resultModel = rateService.queryList("currency.code", "JPY");
//        ChartData result = new ChartData();
//        result.setTime(resultModel.get(0).getDate().getTime()+32400000);
//        Calendar cal = Calendar.getInstance();
//        Date preDate = null;
//        long days = 0;
//        double preRate = 0;
//        for(Rate rate: resultModel){
//            cal.setTime(rate.getDate());
//            if(preDate != null){
//                 days = (rate.getDate().getTime() - preDate.getTime())/(24*60*60*1000);
//            }
//            if(days > 1){
//                for(int i=0; i<days-1; i++){
//                    result.getData().add(preRate);
//                    System.out.println(days);
//                }
//            }
//
//            result.getData().add(rate.getValue());
//
//            preDate = rate.getDate();
//            preRate = rate.getValue();
//        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @RequestMapping(value="/getrate", method=RequestMethod.GET)
    public ResponseEntity<ChartData> getDate(@RequestParam("start") String start, @RequestParam("end") String end,
                                             @RequestParam("in_currency") String inCurrency,
                                             @RequestParam("out_currency") String outCurrency){
        return new ResponseEntity<>(rateService.getHistoryRate(start, end, inCurrency, outCurrency), HttpStatus.OK);
    }
}
