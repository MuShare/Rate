package edu.ut.softlab.rate.dao;

import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Favorite;
import edu.ut.softlab.rate.model.User;

import java.util.List;

/**
 * Created by alex on 16-8-3.
 */
public interface IFavoriteDao extends IOperations<Favorite>{
    List<Favorite> getFavorites(User user);
    String deleteFavoriteByCurrency(Currency currency, User user);
}
