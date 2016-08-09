package edu.ut.softlab.rate.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.bean.SubscribeSyncBean;
import edu.ut.softlab.rate.model.Device;
import edu.ut.softlab.rate.model.Favorite;
import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.service.ICurrencyService;
import edu.ut.softlab.rate.service.IDeviceService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.service.IUserService;

import java.util.*;


@Controller
@RemoteProxy
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userService")
	private IUserService userService;

    @Resource(name = "deviceService")
    private IDeviceService deviceService;

    @Resource(name = "currencyService")
    private ICurrencyService currencyService;



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestParam (value = "uname", required = true)String uname,
                           @RequestParam (value = "email", required = true)String email,
                           @RequestParam(value = "telephone", required = false)String telephone,
                           @RequestParam(value = "password")String password){
        String uid = userService.register(uname, email, telephone, password);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        if(uid != null){
            result.put("uid", uid);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "email has been used");
            response.put(ResponseField.error_code, 300);
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

	@RequestMapping(value="/activate", method = RequestMethod.GET)
	public String activateUser(@RequestParam("validateCode") String validateCode, @RequestParam("uid") String uid ){
		if(userService.Validate(uid, validateCode)){
			return "redirect:/success.html";
		}else{
			return "redirect:/fail.html";
		}
	}

    /**
     * 第一次登录
     * @param email email
     * @param password password
     * @param deviceToken 设备token
     * @param os 操作系统信息
     * @param request 请求实体
     * @return 实体
     */
    @RequestMapping(value="/login", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Map<String, Object>> firstLogin(@RequestParam(value = "email", required = true)String email,
                                                          @RequestParam(value = "password", required = true)String password,
                                                          @RequestParam(value = "device_token", required = true) String deviceToken,
                                                          @RequestParam(value = "os", required = false) String os,
                                                          @RequestParam(value = "did", required = true)String deviceId,
                                                          HttpServletRequest request){


        Map<String, Object> response = new HashMap<>();

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String token = userService.mobileLogin(email, password, deviceToken, os, ip, deviceId);
        if(token != null){
            if(token.equals("pass_error")){
                response.put(ResponseField.error_message, token);
                response.put(ResponseField.error_code, 301);
                response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }else if(token.equals("account_error")){
                response.put(ResponseField.error_message, token);
                response.put(ResponseField.error_code, 302);
                response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }else {
                Map<String, Object> result = new HashMap<>();
                User user = deviceService.findUserByToken(token);
                result.put("token", token);
                result.put("uname", user.getUname());
                result.put("telephone", user.getTelephone());
                result.put("email", user.getEmail());
                Set<Favorite> favorites = user.getFavorites();
                List<String> fav = new ArrayList<>();
                for(Favorite favorite : favorites){
                    fav.add(favorite.getCurrency().getCid());
                }
                result.put("favorite", fav);
                response.put(ResponseField.result, result);
                response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }else {
            response.put(ResponseField.error_message, "login fail");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 二次登录
     * @param deviceToken 设备token
     * @param request 请求实体
     * @return 新token
     */
    @RequestMapping(value = "/login", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> login(@RequestParam(value = "device_token", required = true)String deviceToken,
                                                     HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String currentToken = request.getHeader("token");
        String newToken = deviceService.updateToken(currentToken, deviceToken, ip);
        if(newToken == null){
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else {
            result.put("token", newToken);
            User user = deviceService.findUserByToken(newToken);
            result.put("uname", user.getUname());
            result.put("telephone", user.getTelephone());
            result.put("email", user.getEmail());
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request){
        String token = request.getHeader("token");
        List<Device> devices = deviceService.queryList("loginToken", token);
        System.out.println(token);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        if(devices.size() != 0){
            Device device = devices.get(0);
            device.setLoginToken(null);
            deviceService.update(device);
            result.put("status", "log out successfully");
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 添加订阅
     * @param subscribe subscribe 实体
     * @return 响应实体
     */
    @RequestMapping(value="/subscribe", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSubscribe(Subscribe subscribe,
                                                            @RequestParam(value = "from", required = true)String from,
                                                            @RequestParam(value = "to", required = true)String to,
                                                            @RequestParam(value = "isAbove", required = true)Boolean isAbove,
                                                            @RequestParam(value = "threshold", required = true)Double threshold,
                                                            HttpServletRequest request){
        String token = request.getHeader("token");
        User loggedUser = deviceService.findUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        if(loggedUser != null){
            if(isAbove){
                subscribe.setMax(0.0);
                subscribe.setMin(threshold);
            }else {
                subscribe.setMin(0.0);
                subscribe.setMax(threshold);
            }
            String sid = userService.addSubscribe(subscribe, from, to, loggedUser.getUid());
            result.put("sid", sid);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/subscribe/update", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateSubscribe(Subscribe subscribe,
                                                               @RequestParam(value = "isAbove", required = true)Boolean isAbove,
                                                               @RequestParam(value = "threshold", required = true)Double threshold,
                                                               HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        String token = request.getHeader("token");
        User user = deviceService.findUserByToken(token);
        if(user != null){
            if(isAbove){
                subscribe.setMax(0.0);
                subscribe.setMin(threshold);
            }else {
                subscribe.setMin(0.0);
                subscribe.setMax(threshold);
            }
            String sid = userService.updateSubscribe(subscribe);
            user.setSubscribeRevision(user.getSubscribeRevision()+1);
            userService.update(user);
            result.put("sid", sid);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/subscribe", method = RequestMethod.DELETE)
    ResponseEntity<Map<String, Object>> deleteSubscribe(Subscribe subscribe,
                                                        HttpServletRequest request){

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        String token = request.getHeader("token");
        User user = deviceService.findUserByToken(token);
        if(user != null){
            userService.deleteSubscribe(subscribe);
            user.setSubscribeRevision(user.getSubscribeRevision()+1);
            userService.update(user);
            result.put("isDeleted", true);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/subscribes", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getSubscribes(@RequestBody String sidString,
                                                             HttpServletRequest request) {
        String token = request.getHeader("token");
        User user = deviceService.findUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        JSONObject params = new JSONObject(sidString);
        int rev = params.getInt("rev");
        if(user != null){
            if(rev < user.getSubscribeRevision()){
                JSONArray sidJSONArray = params.getJSONArray("sids");
                Set<String> sids = new HashSet<>();
                for(Object sid : sidJSONArray){
                    sids.add(sid.toString());
                }
                SubscribeSyncBean subscribeSyncBean = userService.getSubscribes(userService.getSubscribs(user), sids, rev);
                result.put("isUpdated", true);
                result.put("data", subscribeSyncBean);
                result.put("current", user.getSubscribeRevision());
                response.put(ResponseField.result, result);
                response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            }else {
                result.put("isUpdated", false);
                response.put(ResponseField.result, result);
                response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping(value = "/favorite", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateFavorites(@RequestParam(value = "cid", required = true) String cid,
                                                               @RequestParam(value = "favorite", required = true) Boolean isFavorite,
                                                               HttpServletRequest request){

        System.out.println(cid+" "+isFavorite);
        String token = request.getHeader("token");
        User user = deviceService.findUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        if(user != null){
            String fid;
            if(isFavorite){
                 fid = userService.addFavorite(currencyService.findOne(cid), user);
            }else {
                fid = userService.deleteFavorite(currencyService.findOne(cid), user);
            }
            result.put("fid", fid);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.error_code, 350);
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
