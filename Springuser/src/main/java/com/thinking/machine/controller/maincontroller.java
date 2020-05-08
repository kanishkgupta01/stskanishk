package com.thinking.machine.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.thinking.machine.dao.mainrepo;
import com.thinking.machine.model.mainuser;

@Controller
public class maincontroller {
	
	@Autowired
	private mainrepo userRepository;

	@RequestMapping(value = {"/","/home"})
	public ModelAndView getHomePage(ModelAndView mv) {

		mv.setViewName("index");

		return mv;

	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ModelAndView getSingUpPage(ModelAndView mv) {

		mv.addObject("user", new mainuser());
		mv.setViewName("signup");

		return mv;

	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ModelAndView addUser(@ModelAttribute("user") mainuser user, ModelAndView modelAndView, HttpSession httpSession) {

		userRepository.save(user);

		httpSession.setAttribute(user.getUserName(), user);
		httpSession.setMaxInactiveInterval(60);

		modelAndView.addObject("user", user);
		modelAndView.setViewName("user");

		return modelAndView;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView modelAndView) {
		modelAndView.addObject("user", new mainuser());
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value = "/validateUser", method = RequestMethod.POST)
	public ModelAndView validateUser(@ModelAttribute("user") mainuser user, ModelAndView modelAndView,
			HttpSession httpSession) {
		mainuser dbUser = userRepository.findByUserName(user.getUserName());

		if (dbUser != null) {
			if (user.getPassword().equals(dbUser.getPassword())) {
				httpSession.setAttribute(user.getUserName(), dbUser);
				httpSession.setMaxInactiveInterval(60);
				modelAndView.setViewName("loginSuccess");
			} else {
				modelAndView.setViewName("loginError");
			}
			return modelAndView;
		}else {
			modelAndView.setViewName("loginError");
			return modelAndView;

		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		httpServletRequest.getSession().invalidate();
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	@RequestMapping(value = "/{userName}")
	public ModelAndView getUserDetails(@PathVariable("userName") String userName, HttpSession httpSession,
			ModelAndView modelAndView) {
		
		mainuser user = (mainuser) httpSession.getAttribute(userName);
		
		try {
			if(user != null) {
				modelAndView.addObject("user", user);
				modelAndView.setViewName("user");
			}else {
				modelAndView.setViewName("sessionExpiredError");
			}
		}catch(IllegalStateException exception) {
			modelAndView.setViewName("sessionExpiredError");
		}
		
		return modelAndView;

	}
}