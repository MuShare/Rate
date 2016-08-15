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
    String addSubscribe(Subscribe subscribe, String fromCid, String toCid, String uid);
    String register(String uname, String email, String telephone, String password);
    boolean Validate(String uid, String validateCode);
    String mobileLogin(String email, String password, String deviceToken, String os, String ip, String deviceId);
    String updateSubscribe(Subscribe subscribe);
    SubscribeSyncBean getSubscribes(List<Subscribe> subscribes, Set<String> sids, int rev);
    List<Subscribe> getSubscribs(User user);
    void deleteSubscribe(Subscribe subscribe);
    String deleteFavorite(Currency currency, User user);
    String addFavorite(Currency currency, User user);
    String addFeedback(User user, int type, String content, String contact);
    String uploadImage(User user, String path, MultipartFile image);
    String sendVerificationCode(User user);
    Boolean changePassword(User user, String password);
    void changeUname(User user, String uname);
    byte[] getAvatar(User user, ServletContext context) throws IOException;
}

