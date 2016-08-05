package edu.ut.softlab.rate.service;

import edu.ut.softlab.rate.bean.SubscribeBean;
import edu.ut.softlab.rate.bean.UserBean;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface IUserService extends IOperations<User> {
//    String Register(User user);
//    boolean Validate(String uid, String validateCode);
//    User Login(User user);
//    String AddSubscribe(Subscribe subscribe, String currencyCode);
//    List<Subscribe> GetSubscribes(User user);
    String addSubscribe(String sname, double min, double max, boolean isEnable, boolean isOnce,
                               boolean isSendEmail, boolean isSendSms, String from, String to, String userId);

    String addSubscribe(Subscribe subscribe, String uid);
    List<SubscribeBean> getSubscribes(HttpSession session);
    SubscribeBean getSubscribe(String sid);
    UserBean checkSession(HttpSession session);
    String register(String uname, String email, String telephone, String password);
    boolean login(String email, String password, HttpSession session);
    boolean editSubscribe(String subscribeSid, String fromCurrencyCid, String toCurrencyCid, String sname, double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms);
    boolean deleteSuscribe(String subscribeSid);
    boolean Validate(String uid, String validateCode);
    String mobileLogin(String email, String password, String deviceToken, String os, String ip, String deviceId);
    String mobileTwiceLogin(String token, String deviceToken, String ip);
}

