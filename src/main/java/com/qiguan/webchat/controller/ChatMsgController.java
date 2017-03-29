package com.qiguan.webchat.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.qiguan.webchat.entity.User;
import com.qiguan.webchat.service.IUserService;

 
/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:40:26  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@Controller
@SessionAttributes("userid")
public class ChatMsgController {
	
	/**
	 * 
	 */
	@Resource
	private User user;
	
	/**
	 * 
	 */
	@Resource
	private IUserService userService;

	/**
	 * 聊天主页
	 */
	@RequestMapping(value = "chat")
	public ModelAndView getIndex() {
		ModelAndView view = new ModelAndView("chat");
		return view;
	}
	
	/**
	 * 聊天主页
	 */
	@RequestMapping(value = "win")
	public ModelAndView win() {
		ModelAndView view = new ModelAndView("win");
		return view;
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "")
	public String index() {
		return "login";
	}

	@RequestMapping(value = "login")
	public String login() {
		return "login";
	}
}
