package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.IFeedbackDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Feedback;
import org.springframework.stereotype.Repository;

/**
 * Created by alex on 16-8-11.
 */

@Repository("feedbackDao")
public class FeedbackDao extends AbstractHibernateDao<Feedback> implements IFeedbackDao{
    public FeedbackDao(){
        super();
        setClass(Feedback.class);
    }
}
