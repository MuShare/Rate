package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.ISubscribeDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by alex on 16-4-11.
 */
@Repository("subscribeDao")
public class SubscribeDaoDao extends AbstractHibernateDao<Subscribe> implements ISubscribeDao {
    public SubscribeDaoDao(){
        super();
        setClass(Subscribe.class);
    }

    @Override
    public List<Subscribe> getSubscribes(User user) {
        Criteria criteria = getCurrentSesstion().createCriteria(Subscribe.class);
        criteria.add(Restrictions.eq("user", user));
        return criteria.list();
    }
}
