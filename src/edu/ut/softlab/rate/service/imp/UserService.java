package edu.ut.softlab.rate.service.imp;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.*;
import edu.ut.softlab.rate.dao.*;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.IRateService;
import edu.ut.softlab.rate.service.IUserService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service("userService")
@RemoteProxy
public class UserService extends AbstractService<User> implements IUserService {
	
	@Resource(name="userDao")
	private IUserDao userDao;

	@Resource(name="currencyDao")
	private ICurrencyDao currencyDao;

	@Resource(name="subscribeDao")
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
	
	public UserService(){
		super();
	}
	
	@Override
	protected IOperations<User> getDao(){
		return userDao;
	}

//	@Override
//	public String Register(User user) {
//		user.setValidateCode(Utility.encode2hex(user.getEmail()));
//		this.dao.create(user);
//		System.out.println(user.getUid());
//		StringBuilder sb = new StringBuilder();
//		sb.append("<a href=\"http://localhost:8080/activate?validateCode=");
//		sb.append(user.getValidateCode());
//		sb.append("&uid=");
//		sb.append(user.getUid());
//		sb.append("</a>");
//		sb.append("在发送邮件的时候，遇到系统退信的情况，后来分析发现是由于中文字符太少，url太长，被邮件系统过滤掉了，加一些汉字和内容就好了；　或者考虑使用企业邮箱");
//		Utility.send(user.getEmail(), sb.toString());
//		return user.getUid();
//	}
//
	@Override
	public boolean Validate(String uid, String validateCode) {
		User user = userDao.findOne(uid);
		if(user != null){
			if(user.getValidateCode().equals(validateCode)){
				user.setStatus(true);
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	@Override
	public String mobileLogin(String email, String password, String deviceToken, String os, String ip, String deviceId) {
		List<User> users = userDao.queryList("email", email);
		if(users.size() == 0){
			return "account_error";
		}else if(users.get(0).getPassword().equals(password)){
			//强制删除登录的情况
			Device device = deviceService.findDeviceByDeviceId(deviceId);
			if(device == null ||
					(device.getLoginToken() == null && device.getUser().getUid().equals(users.get(0).getUid()))){
				device = new Device();
				device.setDeviceToken(deviceToken);
				device.setLastLoginTime(new Date());
				String token = Utility.getToken(email);
				device.setLoginToken(token);
				device.setLastLoginIp(ip);
				device.setUser(users.get(0));
				device.setOsVersion(os);
				device.setDeviceId(deviceId);
				deviceService.create(device);
				return token;
			}else {
				device.setLastLoginTime(new Date());
				device.setLastLoginIp(ip);
				device.setOsVersion(os);
				String token = Utility.getToken(email);
				device.setLoginToken(token);
				device.setDeviceToken(deviceToken);
				return token;
			}
		}else {
			return "pass_error";
		}
	}



	@Override
	public String updateSubscribe(Subscribe updateSubscribe) {
		Subscribe subscribe = subscribeDao.findOne(updateSubscribe.getSid());
		if(subscribe != null){
			subscribe.setSname(updateSubscribe.getSname());
			subscribe.setMax(updateSubscribe.getMax());
			subscribe.setMin(updateSubscribe.getMin());
			subscribe.setIsSendEmail(updateSubscribe.getIsSendEmail());
			subscribe.setIsSendSms(updateSubscribe.getIsSendSms());
			subscribe.setIsEnable(updateSubscribe.getIsEnable());
			subscribe.setRevision(subscribe.getUser().getSubscribeRevision()+1);
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

		for(Subscribe subscribe : subscribes){
			if(subscribe.getRevision() > rev){
				createdOrUpdated.add(new SubscribeMobileBean(subscribe));
			}
			deletedSids.add(subscribe.getSid());
		}
		for(String sid : sids){
			if(!deletedSids.contains(sid)){
				deletedSubscribes.add(sid);
			}
		}

		for(Subscribe subscribe : subscribes){
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
        String storedPath = Utility.imageStorage(path, image);
        user.setAvatar(storedPath);
        update(user);
        return storedPath;
    }

    @Override
    public String sendVerificationCode(User user) {
        Set<Integer> codes = new HashSet<>();
        Random r = new Random();
        while (codes.size() < 4){
            codes.add(r.nextInt(10));
        }
        StringBuilder sb = new StringBuilder();
        for(Integer i : codes){
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
	@RemoteMethod
	public boolean login(String email, String password, HttpSession session) {
		List<User> result = userDao.queryList("email", email);
		if(result.size() == 0){
			return false;
		}else if(result.get(0).getPassword().equals(password)){
			User user = result.get(0);
			UserBean userBean = new UserBean(user.getUid(),user.getUname(),user.getEmail(),user.getPassword());
			session.setAttribute("user", userBean);
			return true;
		}
		return false;
	}

	@Override
	@RemoteMethod
	public boolean editSubscribe(String subscribeSid, String fromCurrencyCid, String toCurrencyCid, String sname, double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms) {
		Subscribe subscribe = subscribeDao.findOne(subscribeSid);
		if(subscribe == null){
			return false;
		}else {
			if(!subscribe.getCurrency().getCid().equals(fromCurrencyCid)){
				Currency currency = currencyDao.findOne(fromCurrencyCid);
				subscribe.setCurrency(currency);
			}
			if(!subscribe.getToCurrency().getCid().equals(toCurrencyCid)){
				Currency currency = currencyDao.findOne(toCurrencyCid);
				subscribe.setCurrency(currency);
			}
			if(!subscribe.getSname().equals(sname)){
				subscribe.setSname(sname);
			}
			if(subscribe.getMin() != min){
				subscribe.setMin(min);
			}
			if(subscribe.getMax() != max){
				subscribe.setMax(max);
			}
			if (subscribe.getIsEnable() != isEnable){
				subscribe.setIsEnable(isEnable);
			}
			if(subscribe.getIsOnce() != isOnce){
				subscribe.setIsOnce(isOnce);
			}
			if(subscribe.getIsSendEmail()){
				subscribe.setIsSendEmail(isSendEmail);
			}
			if(subscribe.getIsSendSms() != isSendSms){
				subscribe.setIsSendSms(isSendSms);
			}
			subscribeDao.update(subscribe);
			return true;
		}
	}

	@Override
	@RemoteMethod
	public boolean deleteSuscribe(String subscribeSid) {
		Subscribe subscribe = subscribeDao.findOne(subscribeSid);
		if(subscribe == null){
			return false;
		}else {
			subscribeDao.delete(subscribe);
			return true;
		}
	}


	@Override
	@RemoteMethod
	public String addSubscribe(String sname, double min, double max, boolean isEnable, boolean isOnce, boolean isSendEmail, boolean isSendSms, String from, String to, String userId) {
		Subscribe subscribe = new Subscribe();
		Currency currency = currencyDao.findOne(from);
		Currency currencyTo = currencyDao.findOne(to);
		User user = userDao.findOne(userId);
		subscribe.setSname(sname);
		subscribe.setUser(user);
		subscribe.setCurrency(currency);
		subscribe.setToCurrency(currencyTo);
		subscribe.setMax(max);
		subscribe.setMin(min);
		subscribe.setIsEnable(isEnable);
		subscribe.setIsOnce(isOnce);
		subscribe.setIsSendEmail(isSendEmail);
		subscribe.setIsSendSms(isSendSms);
		subscribeDao.create(subscribe);
		return subscribe.getSid();
	}

	@Override
	public String addSubscribe(Subscribe subscribe, String fromCid, String toCid, String uid) {
		Currency fromCurrency = currencyDao.findOne(fromCid);
		Currency toCurrency = currencyDao.findOne(toCid);
		User user = userDao.findOne(uid);
		int currentRev = user.getSubscribeRevision();
		subscribe.setRevision(currentRev+1);
		user.setSubscribeRevision(currentRev+1);
		subscribe.setUser(user);
		subscribe.setCurrency(fromCurrency);
		subscribe.setToCurrency(toCurrency);
        subscribe.setDate(new Date());
		subscribeDao.create(subscribe);
		return subscribe.getSid();
	}

	@Override
	@RemoteMethod
	public List<SubscribeBean> getSubscribes(HttpSession session) {
		UserBean user = (UserBean)session.getAttribute("user");
		List<SubscribeBean> result = new ArrayList<>();
		List<Subscribe> list = subscribeDao.queryList("uid", user.getUid());
		for(Subscribe subscribe:list){
			Currency toCurrency = subscribe.getToCurrency();
			Currency fromCurrency = subscribe.getCurrency();
			Rate toCurrencyRate = rateDao.getLatestCurrencyRate(toCurrency);
			Rate fromCurrencyRate = rateDao.getLatestCurrencyRate(fromCurrency);
			double current = Utility.round(fromCurrencyRate.getValue() / toCurrencyRate.getValue(), 5);
			System.out.println(current);
			SubscribeBean subscribeBean = new SubscribeBean(subscribe.getSid(), subscribe.getSname(),new CurrencyBean(subscribe.getCurrency()),
					new CurrencyBean(subscribe.getToCurrency()),user,subscribe.getMin(),subscribe.getMax(),subscribe.getIsEnable(),
							subscribe.getIsOnce(), subscribe.getIsSendEmail(), subscribe.getIsSendSms(),current);
			result.add(subscribeBean);
		}
		return result;
	}

	@Override
	public SubscribeBean getSubscribe(String sid) {
		Subscribe subscribe=subscribeDao.findOne(sid);
		if(subscribe==null) {
			return null;
		}
		return null;
	}

	@Override
	@RemoteMethod
	public UserBean checkSession(HttpSession session) {
		UserBean user = (UserBean)session.getAttribute("user");
		if(user == null){
			return null;
		}else {
			System.out.println(user.getEmail());
			return user;
		}
	}

	@Override
	@RemoteMethod
	public String register(String uname, String email, String telephone, String password) {
		if(userDao.queryList("email", email).size() == 0){
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
			Thread sendMail = new Thread(new SendMail(user.getEmail(), "Active your email",sb.toString()));
			sendMail.start();
			return user.getUid();
		}else {
			return null;
		}
	}

	private class SendMail implements Runnable{
		private String email;
		private String content;
		private String subject;
		public SendMail(String email, String subject, String content){
			this.email = email;
			this.content = content;
			this.subject = subject;
		}
		@Override
		public void run() {
			Utility.send(email, subject, content);
		}
	}

    private class SendVerifyCode implements Runnable{
        private String email;
        private String code;
        public SendVerifyCode(String email, String code){
            this.email = email;
            this.code = code;
        }

        @Override
        public void run() {
            Utility.send(email, "Verification Code", code);
        }
    }
}
