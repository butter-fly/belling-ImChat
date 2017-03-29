package com.qiguan.webchat.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**  
 * <pre>
 * Description	登陆身份过滤器
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:50:35  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public class LoginFilter extends OncePerRequestFilter {
	
	/* (non-Javadoc)
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String[] notFilter = new String[] { "login", "login.jsp", "user/logout", "user/login" }; // 不过滤的uri
		String contextPath = request.getContextPath(); // 主路径
		String uri = request.getRequestURI(); // 请求的uri
		// uri中包含jsp时才进行过滤
		if (uri.indexOf("jsp") != -1) {
			// 是否过滤
			boolean doFilter = true;
			for (String s : notFilter) {
				if (uri.indexOf(s) != -1) {
					// 如果uri中包含不过滤的uri，则不进行过滤
					doFilter = false;
					break;
				}
			}
			if (doFilter) {
				// 执行过滤
				if (null == request.getSession().getAttribute("login_status")) {
					// System.out.println(uri+">>>>>>>=>过滤器执行过滤");
					response.sendRedirect(contextPath + "/login?timeout=true");
				} else {
					// 如果session中存在登录者实体，则继续
					// System.out.println(uri+">>>>>>>=>过滤器=>已登录");
					filterChain.doFilter(request, response);
				}
			} else {
				// 如果不执行过滤，则继续
				// System.out.println(uri+">>>>>>>=>过滤器自动忽略");
				filterChain.doFilter(request, response);
			}
		} else {
			// 如果uri中不包含jsp，则继续
			filterChain.doFilter(request, response);
		}
	}

}
