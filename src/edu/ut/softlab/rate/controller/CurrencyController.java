package edu.ut.softlab.rate.controller;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

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

    private final String flagPath = "/web/static/img/flags/";

    @RequestMapping(value="", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getCurrencies(@RequestParam(value="cid", required = false) String cid,
                                                            @RequestParam(value="lan", required = false, defaultValue = "en") String lan,
                                                             HttpServletRequest request){
        List<CurrencyBean> result = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        if (cid == null){
            List<Currency> currencyList = currencyService.getCurrencyList();
            for(Currency currency : currencyList){
                CurrencyBean currencyBean = new CurrencyBean(currency);
                System.out.println(currency.getCode());
                ServletContext context = request.getServletContext();
                String rootPath = context.getRealPath(File.separator);
                currencyBean.setIcon(Utility.svgToString(rootPath+"/static/img/flags/ad.svg"));
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                result.add(currencyBean);
            }
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            Currency currency = currencyService.findOne(cid);
            if(currency == null){
                response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST);
                response.put(ResponseField.error_message, "No requested currency");
                return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
            }else {
                CurrencyBean currencyBean = new CurrencyBean(currency);
                currencyBean.setIcon(flagPath+currencyCountry.get(currency.getCode()).toString().toLowerCase()+".png");
                currencyBean.setName(java.util.Currency.getInstance(currency.getCode()).getDisplayName(Locale.forLanguageTag(lan)));
                result.add(currencyBean);
                response.put(ResponseField.result, result);
                response.put(ResponseField.HttpStatus, HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
    }
}
