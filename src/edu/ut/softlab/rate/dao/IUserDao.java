package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.dao.common.IOperations;

public interface IUserDao extends IOperations<User> {

    /**
     *
     * @param email
     * @return
     */
    User getByEmail(String email);
}
