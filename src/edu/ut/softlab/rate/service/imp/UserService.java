package edu.ut.softlab.rate.service.imp;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.bean.SubscribeBean;
import edu.ut.softlab.rate.bean.UserBean;
import edu.ut.softlab.rate.dao.*;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.model.*;
import edu.ut.softlab.rate.service.IDeviceService;
import edu.ut.softlab.rate.service.IUserService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			return null;
		}else if(users.get(0).getPassword().equals(password)){
			//强制删除登录的情况
			Device device = deviceService.findDeviceByDeviceId(deviceId);
			if(device == null){
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
			return null;
		}
	}

	@Override
	public String mobileTwiceLogin(String token, String deviceToken, String ip) {
		String newToken = deviceService.updateToken(token, deviceToken, ip);
		return newToken;
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
	public String addSubscribe(Subscribe subscribe, String uid) {
		User user = userDao.findOne(uid);
		subscribe.setUser(user);
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
		User user = new User();
		user.setUname(uname);
		user.setPassword(password);
		user.setEmail(email);
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
		Thread sendMail = new Thread(new SendMail(user.getEmail(), sb.toString()));
		sendMail.start();
		return user.getUid();
	}

	private class SendMail implements Runnable{
		private String email;
		private String content;
		public SendMail(String email, String content){
			this.email = email;
			this.content = content;
		}
		@Override
		public void run() {
			Utility.send(email, content);
		}
	}
}
