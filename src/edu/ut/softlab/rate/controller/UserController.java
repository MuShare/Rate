package edu.ut.softlab.rate.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.ut.softlab.rate.model.Subscribe;
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

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestParam(value = "email", required = true)String email,
                                                     @RequestParam(value = "password", required = true)String password,
                                                     @RequestParam(value = "device_token", required = true) String deviceToken,
                                                     @RequestParam(value = "os", required = false) String os, HttpServletRequest request){

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
        System.out.println(ip);
        String token = userService.mobileLogin(email, password, deviceToken, os, ip);
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
}
