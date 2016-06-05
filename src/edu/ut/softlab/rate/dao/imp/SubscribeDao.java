package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.ISubscribe;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Subscribe;
import org.springframework.stereotype.Repository;


/**
 * Created by alex on 16-4-11.
 */
@Repository("subscribeDao")
public class SubscribeDao extends AbstractHibernateDao<Subscribe> implements ISubscribe{
    public SubscribeDao(){
        super();
        setClass(Subscribe.class);
    }
}
