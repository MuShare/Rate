package edu.ut.softlab.rate.service.imp;

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

    @Override
    protected IOperations<Currency> getDao() {
        return this.getDao();
    }

    @Override
    @RemoteMethod
    public List<Currency> getCurrencyList() {
        return currencyDao.findAll();
    }
}
