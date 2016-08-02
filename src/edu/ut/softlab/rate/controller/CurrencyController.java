package edu.ut.softlab.rate.controller;

import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Created by alex on 16-8-1.
 */
@Controller
@RequestMapping("/currencies")
public class CurrencyController {
    @Resource(name = "currencyService")
    private ICurrencyService currencyService;

    @Value("#{currency_country}")
    private Properties currencyCountry;

    private final String flagPath = "static/img/flags";

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<List<CurrencyBean>> getCurrencies(@RequestParam(value="cid", required = false) String cid,
                                                            @RequestParam(value="lan", required = false, defaultValue = "en") String lan){
        List<CurrencyBean> result = new ArrayList<>();
        if (cid == null){
            List<Currency> currencyList = currencyService.getCurrencyList();
            for(Currency currency : currencyList){
                CurrencyBean currencyBean = new CurrencyBean(currency);
                currencyBean.setIcon(flagPath+currencyCountry.get(currency.getCode()).toString().toLowerCase()+".png");
                System.out.println(currency.getCode());
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                result.add(currencyBean);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            Currency currency = currencyService.findOne(cid);
            if(currency == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else {
                CurrencyBean currencyBean = new CurrencyBean(currency);
                currencyBean.setIcon(flagPath+currencyCountry.get(currency.getCode()).toString().toLowerCase()+".png");
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                result.add(currencyBean);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
    }
}
