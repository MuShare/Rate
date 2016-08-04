package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.ISubscribeDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Subscribe;
import org.springframework.stereotype.Repository;


/**
 * Created by alex on 16-4-11.
 */
@Repository("subscribeDao")
public class SubscribeDaoDao extends AbstractHibernateDao<Subscribe> implements ISubscribeDao {
    public SubscribeDaoDao(){
        super();
        setClass(Subscribe.class);
    }
}
