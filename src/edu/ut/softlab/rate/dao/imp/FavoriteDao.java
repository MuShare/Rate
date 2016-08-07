package edu.ut.softlab.rate.dao.imp;

import edu.ut.softlab.rate.dao.IFavoriteDao;
import edu.ut.softlab.rate.dao.common.AbstractHibernateDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Favorite;
import edu.ut.softlab.rate.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by alex on 16-8-3.
 */
@Repository("favoriteDao")
public class FavoriteDao extends AbstractHibernateDao<Favorite> implements IFavoriteDao{
    public FavoriteDao(){
        super();
        setClass(Favorite.class);
    }

    @Override
    public List<Favorite> getFavorites(User user) {
        Criteria criteria = getCurrentSesstion().createCriteria(Favorite.class);
        criteria.add(Restrictions.eq("user", user));
        return criteria.list();
    }

    @Override
    public String deleteFavoriteByCurrency(Currency currency, User user) {
        Criteria criteria = getCurrentSesstion().createCriteria(Favorite.class);
        criteria.add(Restrictions.eq("currency", currency))
                .add(Restrictions.eq("user", user));
        Favorite deletedFav = (Favorite)criteria.uniqueResult();
        if(deletedFav != null){
            String fid = deletedFav.getFid();
            delete(deletedFav);
            return fid;
        }else {
            return null;
        }
    }
}
