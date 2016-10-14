package edu.ut.softlab.rate.service.imp;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.*;
import edu.ut.softlab.rate.component.ApplicationContextProvider;
import edu.ut.softlab.rate.dao.*;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.IRateService;
import edu.ut.softlab.rate.service.IUserService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.apache.commons.io.IOUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service("userService")
public class UserService extends AbstractService<User> implements IUserService {

    @Resource(name = "userDao")
    private IUserDao userDao;

    @Resource(name = "currencyDao")
    private ICurrencyDao currencyDao;

    @Resource(name = "subscribeDao")
    private ISubscribeDao subscribeDao;

    @Resource(name = "rateDao")
    private IRateDao rateDao;

    @Resource(name = "deviceDao")
    private IDeviceDao deviceDao;

    @Resource(name = "deviceService")
    private IDeviceService deviceService;

    @Resource(name = "rateService")
    private IRateService rateService;

    @Resource(name = "favoriteDao")
    private IFavoriteDao favoriteDao;

    @Resource(name = "feedbackDao")
    private IFeedbackDao feedbackDao;

    @Resource
    private ResourceLoader resourceLoader;

    public UserService() {
        super();
    }

    @Override
    protected IOperations<User> getDao() {
        return userDao;
    }

    @Override
    public boolean Validate(String uid, String validateCode) {
        User user = userDao.findOne(uid);
        if (user != null) {
            if (user.getValidateCode().equals(validateCode)) {
                user.setStatus(true);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String register(String uname, String email, String telephone, String password) {
        if (userDao.queryList("email", email).size() == 0) {
            User user = new User();
            user.setUname(uname);
            user.setPassword(password);
            user.setEmail(email);
            user.setSubscribeRevision(0);
            user.setFavoriteRevision(0);
            user.setTelephone(telephone);
            user.setLoginDate(new Date());
            user.setValidateCode(Utility.getToken(user.getEmail()));
            this.userDao.create(user);
            StringBuilder sb = new StringBuilder();
            sb.append("please click the following url to validate your email address,please click \n");
            sb.append("href=\"http://rate.mushare.cn/api/user/activate?validateCode=");
            sb.append(user.getValidateCode());
            sb.append("&uid=");
            sb.append(user.getUid());
            Thread sendMail = new Thread(new SendMail(user.getEmail(), "Active your email", sb.toString()));
            sendMail.start();
            return user.getUid();
        } else {
            return null;
        }
    }


    @Override
    public String mobileLogin(String email, String password, String deviceToken, String os, String ip, String deviceId) {
        List<User> users = userDao.queryList("email", email);
        if (users.size() == 0) {
            return "account_error";
        } else if (users.get(0).getPassword().equals(password)) {
            //强制删除登录的情况
            List<Device> devices = deviceService.findDeviceByDeviceId(deviceId);

            Device currentUserDevice = null;
            for (Device device : devices) {
                if (device.getUser().getUid().equals(users.get(0).getUid())) {
                    currentUserDevice = device;
                    break;
                }
            }
            if(currentUserDevice != null){
                currentUserDevice.setLastLoginTime(new Date());
                currentUserDevice.setLastLoginIp(ip);
                currentUserDevice.setIsNotify(true);
                currentUserDevice.setOsVersion(os);
                String token = Utility.getToken(currentUserDevice.getLoginToken());
                currentUserDevice.setLoginToken(token);
                currentUserDevice.setDeviceToken(deviceToken);
                deviceService.update(currentUserDevice);
                return token;
            }else {
                Device device = new Device();
                device.setDeviceToken(deviceToken);
                device.setLastLoginTime(new Date());
                String token = Utility.getToken(email);
                device.setIsNotify(true);
                device.setLoginToken(token);
                device.setLastLoginIp(ip);
                device.setUser(users.get(0));
                device.setOsVersion(os);
                device.setDeviceId(deviceId);
                deviceService.create(device);
                return token;
            }
        } else {
            return "pass_error";
        }
    }


    @Override
    public String updateSubscribe(Subscribe updateSubscribe) {
        Subscribe subscribe = subscribeDao.findOne(updateSubscribe.getSid());
        if (subscribe != null) {
            subscribe.setSname(updateSubscribe.getSname());
            subscribe.setMax(updateSubscribe.getMax());
            subscribe.setMin(updateSubscribe.getMin());
            subscribe.setIsSendEmail(updateSubscribe.getIsSendEmail());
            subscribe.setIsSendSms(updateSubscribe.getIsSendSms());
            subscribe.setIsEnable(updateSubscribe.getIsEnable());
            subscribe.setRevision(subscribe.getUser().getSubscribeRevision() + 1);
            subscribeDao.update(subscribe);
            return subscribe.getSid();
        }
        return null;
    }

    @Override
    public SubscribeSyncBean getSubscribes(List<Subscribe> subscribes, Set<String> sids, int rev) {
        List<SubscribeMobileBean> createdOrUpdated = new ArrayList<>();
        List<String> deletedSubscribes = new ArrayList<>();
        Map<String, Double> rates = new HashMap<>();

        SubscribeSyncBean subscribeSyncBean = new SubscribeSyncBean();
        Set<String> deletedSids = new HashSet<>();

        for (Subscribe subscribe : subscribes) {
            if (subscribe.getRevision() > rev) {
                createdOrUpdated.add(new SubscribeMobileBean(subscribe));
            }
            deletedSids.add(subscribe.getSid());
        }
        for (String sid : sids) {
            if (!deletedSids.contains(sid)) {
                deletedSubscribes.add(sid);
            }
        }

        for (Subscribe subscribe : subscribes) {
            double rate = rateService.getCurrentRate(subscribe.getCurrency().getCid(), subscribe.getToCurrency().getCid());
            rates.put(subscribe.getSid(), rate);
        }

        subscribeSyncBean.setCreatedOrUpdated(createdOrUpdated);
        subscribeSyncBean.setDeletedSubcribes(deletedSubscribes);
        subscribeSyncBean.setRates(rates);
        return subscribeSyncBean;
    }


    @Override
    public List<Subscribe> getSubscribs(User user) {
        return subscribeDao.getSubscribes(user);
    }

    @Override
    public void deleteSubscribe(Subscribe subscribe) {
        subscribeDao.delete(subscribe);
    }

    @Override
    public String deleteFavorite(Currency currency, User user) {
        return favoriteDao.deleteFavoriteByCurrency(currency, user);
    }

    @Override
    public String addFavorite(Currency currency, User user) {
        Favorite favorite = new Favorite();
        favorite.setCurrency(currency);
        favorite.setUser(user);
        favoriteDao.create(favorite);
        return favorite.getFid();
    }

    @Override
    public String addFeedback(User user, int type, String content, String contact) {
        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setType(type);
        feedback.setContent(content);
        feedback.setContact(contact);
        feedback.setDate(new Date());
        feedbackDao.create(feedback);
        return feedback.getFdid();
    }

    @Override
    public String uploadImage(User user, String path, MultipartFile image) {
        if (user.getAvatar() != null) {
            File obsoleteImage = new File(path + user.getAvatar());
            obsoleteImage.delete();
        }
        String storedPath = Utility.imageStorage(path, image);
        if (!storedPath.equals("size error")) {
            user.setAvatar(storedPath);
            user.setAvatarRevision(user.getAvatarRevision() + 1);
            update(user);
        }
        return storedPath;
    }

    @Override
    public byte[] getAvatar(User user, ServletContext context) throws IOException {
        InputStream is = context.getResourceAsStream(user.getAvatar());
        return IOUtils.toByteArray(is);
    }

    @Override
    public String sendVerificationCode(User user) {
        Set<Integer> codes = new HashSet<>();
        Random r = new Random();
        while (codes.size() < 4) {
            codes.add(r.nextInt(10));
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : codes) {
            sb.append(i);
        }
        SendVerifyCode sendVerifyCode = new SendVerifyCode(user.getEmail(), sb.toString());
        Thread sendCodeThread = new Thread(sendVerifyCode);
        sendCodeThread.start();
        user.setValidateCode(sb.toString());
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.MINUTE, 30);
        user.setCodeExpiration(cl.getTime());
        user.setVerificationCode(sb.toString());
        update(user);
        return sb.toString();
    }

    @Override
    public Boolean changePassword(User user, String password) {
        user.setPassword(password);
        update(user);
        return true;
    }

    @Override
    public void changeUname(User user, String uname) {
        user.setUname(uname);
        update(user);
    }

    @Override
    public String addSubscribe(Subscribe subscribe, String fromCid, String toCid, String uid) {
        Currency fromCurrency = currencyDao.findOne(fromCid);
        Currency toCurrency = currencyDao.findOne(toCid);
        User user = userDao.findOne(uid);
        int currentRev = user.getSubscribeRevision();
        subscribe.setRevision(currentRev + 1);
        user.setSubscribeRevision(currentRev + 1);
        subscribe.setUser(user);
        subscribe.setCurrency(fromCurrency);
        subscribe.setToCurrency(toCurrency);
        subscribe.setDate(new Date());
        subscribeDao.create(subscribe);
        return subscribe.getSid();
    }


    private class SendMail implements Runnable {
        private String email;
        private String content;
        private String subject;

        public SendMail(String email, String subject, String content) {
            this.email = email;
            this.content = content;
            this.subject = subject;
        }

        @Override
        public void run() {
            Utility.send(email, subject, content);
        }
    }

    private class SendVerifyCode implements Runnable {
        private String email;
        private String code;

        public SendVerifyCode(String email, String code) {
            this.email = email;
            this.code = code;
        }

        @Override
        public void run() {
            Utility.send(email, "Verification Code", code);
        }
    }
}
