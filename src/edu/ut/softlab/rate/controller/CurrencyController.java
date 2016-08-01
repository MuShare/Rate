package edu.ut.softlab.rate.controller;

import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 16-8-1.
 */
@Controller
@RequestMapping("/currencies")
public class CurrencyController {
    @Resource(name = "currencyService")
    private ICurrencyService currencyService;

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<List<CurrencyBean>> getCurrencies(@RequestParam(value="cid", required = false) String cid){
        List<CurrencyBean> result = new ArrayList<>();
        if (cid == null){
            List<Currency> currencyList = currencyService.getCurrencyList();
            for(Currency currency : currencyList){
                result.add(new CurrencyBean(currency));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            Currency currency = currencyService.findOne(cid);
            if(currency == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else {
                result.add(new CurrencyBean(currency));
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }
}
