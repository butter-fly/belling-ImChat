package com.qiguan.webchat.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**  
 * <pre>
 * Description	登陆拦截器
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午5:00:19  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	/**
	 * 忽略的URI
	 */
	private static final String[] IGNORE_URI = { "user/login", "/login", "/user/logout" };
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		for (String s : IGNORE_URI) {
			if (url.contains(s)) {
				 System.out.printf(url + "=>>>>>=>拦截器已自动忽略!" + "\n");
				return true;
			}
		}
		HttpSession session = request.getSession();
		if (session != null && session.getAttribute("login_status") != null) {
			System.out.printf(url + "=>>>>>拦截器=>已登录,欢迎访问!" + "\n");
			return true;
		} else {
			System.out.printf(url + "=>>>>>拦截器=>未登录,已拦截!" + "\n");
			response.sendRedirect(contextPath + "/login?timeout=true");
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
