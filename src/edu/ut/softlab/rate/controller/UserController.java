package edu.ut.softlab.rate.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.model.Subscribe;
import edu.ut.softlab.rate.service.IDeviceService;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import edu.ut.softlab.rate.model.User;
import edu.ut.softlab.rate.service.IUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RemoteProxy
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userService")
	private IUserService userService;

    @Resource(name = "deviceService")
    private IDeviceService deviceService;



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestParam (value = "uname", required = true)String uname,
                           @RequestParam (value = "email", required = true)String email,
                           @RequestParam(value = "telephone", required = false)String telephone,
                           @RequestParam(value = "password")String password){
        String uid = userService.register(uname, email, telephone, password);
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        response.put(ResponseField.result, result);
        response.put(ResponseField.HttpStatus, HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

	@RequestMapping(value="/activate", method = RequestMethod.GET)
	public String activateUser(@RequestParam("validateCode") String validateCode, @RequestParam("uid") String uid ){
		ModelAndView mv = new ModelAndView();
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
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "login fail");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 二次登录
     * @param currentToken 现有token
     * @param deviceToken 设备token
     * @param request 请求实体
     * @return 新token
     */
    @RequestMapping(value = "/login", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> login(@RequestParam(value = "login_token", required = true)String currentToken,
                                                     @RequestParam(value = "device_token", required = true)String deviceToken,
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

        String newToken = deviceService.updateToken(currentToken, deviceToken, ip);
        if(newToken == null){
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }else {
            result.put("token", newToken);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    /**
     * 添加订阅
     * @param subscribe subscribe 实体
     * @param uid 用户id
     * @param token 用户token
     * @return 响应实体
     */
    @RequestMapping(value="/subscribe", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSubscribe(Subscribe subscribe,
                                                            @RequestParam(value = "uid", required = true)String uid,
                                                            @RequestParam(value = "token", required = true)String token){
        User loggedUser = deviceService.findUserByToken(token);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        if(loggedUser != null && loggedUser.getUid().equals(uid)){
            String sid = userService.addSubscribe(subscribe, uid);
            result.put("sid", sid);
            response.put(ResponseField.result, result);
            response.put(ResponseField.HttpStatus, HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else {
            response.put(ResponseField.error_message, "token error");
            response.put(ResponseField.HttpStatus, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
