package edu.ut.softlab.rate.service.imp;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.Utility;
import edu.ut.softlab.rate.bean.CurrencyBean;
import edu.ut.softlab.rate.bean.SubscribeBean;
import edu.ut.softlab.rate.bean.UserBean;
import edu.ut.softlab.rate.dao.ICurrencyDao;
import edu.ut.softlab.rate.dao.IRateDao;
import edu.ut.softlab.rate.dao.ISubscribe;
import edu.ut.softlab.rate.dao.common.IOperations;
import edu.ut.softlab.rate.dao.IUserDao;
import edu.ut.softlab.rate.model.Currency;
import edu.ut.softlab.rate.model.Rate;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.service.IUserService;
import edu.ut.softlab.rate.service.common.AbstractService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

import edu.ut.softlab.rate.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("userService")
@RemoteProxy
public class UserService extends AbstractService<User> implements IUserService {
	
	@Resource(name="userDao")
	private IUserDao dao;

	@Resource(name="currencyDao")
	private ICurrencyDao currencyDao;

	@Resource(name="subscribeDao")
	private ISubscribe subscribeDao;

	@Resource(name = "rateDao")
	private IRateDao rateDao;
	
	public UserService(){
		super();
	}
	
	@Override
	protected IOperations<User> getDao(){
		return this.dao;
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
//	@Override
//	public boolean Validate(String uid, String validateCode) {
//		User user = this.dao.findOne(uid);
//		if(user != null){
//			return user.getValidateCode().equals(validateCode);
//		}else {
//			return false;
//		}
//	}
//
//	@Override
//	public User Login(User user) {
//		List<User> result = dao.queryList("email", user.getEmail());
//		if(result.size() == 0){
//			return null;
//		}else if(result.get(0).getPassword().equals(user.getPassword())){
//			return result.get(0);
//		}
//		return null;
//	}


	@Override
	@RemoteMethod
	public boolean login(String email, String password, HttpSession session) {
		List<User> result = dao.queryList("email", email);
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
		User user = dao.findOne(userId);
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
			SubscribeBean subscribeBean = new SubscribeBean(subscribe.getSid(), subscribe.getSname(),new CurrencyBean(subscribe.getCurrency().getCid(),subscribe.getCurrency().getCode()),
					new CurrencyBean(subscribe.getToCurrency().getCid(),subscribe.getToCurrency().getCode()),user,subscribe.getMin(),subscribe.getMax(),subscribe.getIsEnable(),
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
		user.setValidateCode(Utility.encode2hex(user.getEmail()));
		this.dao.create(user);
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"http://localhost:8080/activate?validateCode=");
		sb.append(user.getValidateCode());
		sb.append("&uid=");
		sb.append(user.getUid());
		sb.append("</a>");
		sb.append("在发送邮件的时候，遇到系统退信的情况，后来分析发现是由于中文字符太少，url太长，被邮件系统过滤掉了，加一些汉字和内容就好了；　或者考虑使用企业邮箱");
		Utility.send(user.getEmail(), sb.toString());
		return user.getUid();
	}
}
