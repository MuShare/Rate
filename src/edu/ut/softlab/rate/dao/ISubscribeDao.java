package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;
import java.util.List;

/**
 * Created by alex on 16-4-11.
 */
public interface ISubscribeDao extends IOperations<Subscribe> {
    List<Subscribe> getSubscribes(User user);
}
