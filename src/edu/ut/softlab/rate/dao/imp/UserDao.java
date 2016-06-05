package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.IUserDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao extends AbstractHibernateDao<User> implements IUserDao {
	public UserDao() {
        super();
        setClass(User.class);
    }
}
