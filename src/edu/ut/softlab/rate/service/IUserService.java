package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.bean.SubscribeSyncBean;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IUserService extends IOperations<User> {

    /**
     *
     * @param subscribe
     * @param fromCid
     * @param toCid
     * @param uid
     * @return
     */
    String addSubscribe(Subscribe subscribe, String fromCid, String toCid, String uid);

    /**
     *
     * @param uname
     * @param email
     * @param telephone
     * @param password
     * @return
     */
    String register(String uname, String email, String telephone, String password);

    /**
     *
     * @param uid
     * @param validateCode
     * @return
     */
    boolean Validate(String uid, String validateCode);

    /**
     *
     * @param email
     * @param password
     * @param deviceToken
     * @param os
     * @param ip
     * @param deviceId
     * @return
     */
    String mobileLogin(String email, String password, String deviceToken, String os, String ip, String deviceId);

    /**
     *
     * @param subscribe
     * @return
     */
    String updateSubscribe(Subscribe subscribe);

    /**
     *
     * @param subscribes
     * @param sids
     * @param rev
     * @return
     */
    SubscribeSyncBean getSubscribes(List<Subscribe> subscribes, Set<String> sids, int rev);

    /**
     *
     * @param user
     * @return
     */
    List<Subscribe> getSubscribs(User user);

    /**
     *
     * @param subscribe
     */
    void deleteSubscribe(Subscribe subscribe);

    /**
     *
     * @param currency
     * @param user
     * @return
     */
    String deleteFavorite(Currency currency, User user);

    /**
     *
     * @param currency
     * @param user
     * @return
     */
    String addFavorite(Currency currency, User user);

    /**
     *
     * @param user
     * @param type
     * @param content
     * @param contact
     * @return
     */
    String addFeedback(User user, int type, String content, String contact);

    /**
     *
     * @param user
     * @param path
     * @param image
     * @return
     */
    String uploadImage(User user, String path, MultipartFile image);

    /**
     * Get a user by his email address
     * @param email
     * @return
     */
    User getUserByEmail(String email);

    /**
     *
     * @param user
     * @return
     */
    String sendVerificationCode(User user);

    /**
     *
     * @param user
     * @param password
     * @return
     */
    Boolean changePassword(User user, String password);

    /**
     *
     * @param user
     * @param uname
     */
    void changeUname(User user, String uname);

    /**
     *
     * @param user
     * @param context
     * @return
     * @throws IOException
     */
    byte[] getAvatar(User user, ServletContext context) throws IOException;
}

