package edu.ut.softlab.rate.controller;

import javax.annotation.Resource;
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
import java.util.List;


@Controller
@RemoteProxy
public class UserController {

//	@Resource(name = "userService")
//	private IUserService userService;
//
//	@RequestMapping(value="/activate", method = RequestMethod.GET)
//	public ModelAndView activateUser(@RequestParam("validateCode") String validateCode, @RequestParam("uid") String uid ){
//		ModelAndView mv = new ModelAndView();
//		if(userService.Validate(uid, validateCode)){
//			mv.setViewName("success");
//		}else{
//			mv.setViewName(("fail"));
//		}
//		return mv;
//	}
//
//	@RemoteMethod
//	@RequestMapping(value="/login", method = RequestMethod.POST)
//	public ResponseEntity<Boolean> login(@ModelAttribute("user") User user, HttpSession session){
//		User result = userService.Login(user);
//		if(result != null){
//			session.setAttribute("user", result);
//			return new ResponseEntity<>(true, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(false, HttpStatus.OK);
//	}
//
//	@RequestMapping(value="/register", method=RequestMethod.POST)
//	public ModelAndView add(@ModelAttribute("user") User user){
//		System.out.println(user.getEmail()+" sdf");
//		userService.Register(user);
//		return new ModelAndView("/register.html");
//	}
//
//	@RequestMapping(value="/register", method=RequestMethod.GET)
//	public ModelAndView showRegister(@ModelAttribute("user") User user){
//		return new ModelAndView("/register.html");
//	}
//
//
//	@Transactional
//	@RequestMapping(value="/subscribe", method=RequestMethod.GET)
//	public ModelAndView getSubscribe(HttpSession session){
//		User user = (User)session.getAttribute("user");
//		ModelAndView mv = new ModelAndView("subscribe");
//		List<Subscribe> subscribes = userService.GetSubscribes(user);
//		mv.addObject("subscribes", subscribes);
//		return mv;
//	}
//
//	@RequestMapping(value="/addscribe", method=RequestMethod.POST)
//	public ModelAndView addSubscribe(@ModelAttribute("subscribe") Subscribe subscribe, HttpSession session){
//		User user = (User)session.getAttribute("user");
//		subscribe.setUser(user);
//		System.out.println(subscribe.getSname());
//		userService.AddSubscribe(subscribe, "JPY");
//		return new ModelAndView("redirect:/subscribe");
//	}
}
