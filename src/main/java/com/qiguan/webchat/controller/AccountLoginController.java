package com.qiguan.webchat.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qiguan.webchat.entity.User;
import com.qiguan.webchat.service.IUserService;
import com.qiguan.webchat.utils.RequestUtil;
import com.qiguan.webchat.utils.ContantsDefined;

 
/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:43:27  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@Controller
@RequestMapping(value = "user")
public class AccountLoginController {
	
	/**
	 * 用户对象
	 */
	@Resource
	private User user;
	
	/**
	 * 用户业务对象
	 */
	@Resource
	private IUserService userService;

	/**
	 * @param userid
	 * @param password
	 * @param session
	 * @param attributes
	 * @param defined
	 * @param netUtil
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(String userid, String password, HttpSession session, RedirectAttributes attributes,
			ContantsDefined defined, RequestUtil netUtil, HttpServletRequest request) {
		user = userService.selectUserByUserid(userid);
		if (user == null) {
			attributes.addFlashAttribute("error", defined.LOGIN_USERID_ERROR);
			return "redirect:/login";
		} else {
			if (!user.getPassword().equals(password)) {
				attributes.addFlashAttribute("error", defined.LOGIN_PASSWORD_ERROR);
				return "redirect:/login";
			} else {
				if (user.getStatus() != 1) {
					attributes.addFlashAttribute("error", defined.LOGIN_USERID_DISABLED);
					return "redirect:/login";
				} else {
					session.setAttribute("userid", userid);
					session.setAttribute("login_status", true);
					attributes.addFlashAttribute("message", defined.LOGIN_SUCCESS);
					return "redirect:/chat";
				}
			}
		}
	}

	/**
	 * @param session
	 * @param attributes
	 * @param defined
	 * @return
	 */
	@RequestMapping(value = "logout")
	public String logout(HttpSession session, RedirectAttributes attributes, ContantsDefined defined) {
		session.removeAttribute("userid");
		session.removeAttribute("login_status");
		attributes.addFlashAttribute("message", defined.LOGOUT_SUCCESS);
		return "redirect:/login";
	}
}
