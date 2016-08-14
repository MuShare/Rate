package edu.ut.softlab.rate.service.imp;

import edu.ut.softlab.rate.component.UpdateData;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by alex on 16-5-3.
 */
@RemoteProxy
@Service("currencyService")
public class CurrencyService  extends AbstractService<Currency> implements ICurrencyService{
    @Resource(name="currencyDao")
    private ICurrencyDao currencyDao;

    @Resource(name = "updateData")
    private UpdateData updateData;

    @Override
    protected IOperations<Currency> getDao() {
        return currencyDao;
    }

    @Override
    @RemoteMethod
    public List<Currency> getCurrencyList() {
        return currencyDao.findAll();
    }

    @Override
    public List<Currency> getUpdatedCurrencies(int rev) {
        return currencyDao.getUpdatedCurrencies(rev);
    }

    @Override
    public int getCurrentRevision() {
        return currencyDao.getCurrentRev();
    }

    @Override
    public String addCurrency(String code) {
        return updateData.addCurrencyAndRate(code);
    }
}
