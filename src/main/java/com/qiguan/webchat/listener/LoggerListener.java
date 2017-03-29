package com.qiguan.webchat.listener;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContextEvent;

/**  
 * <pre>
 * Description	将JDK自带的jul日志桥接到log4j中,使得启动日志被log4j接管
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午5:03:22  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@SuppressWarnings("deprecation")
public class LoggerListener extends Log4jConfigListener {

	/* (non-Javadoc)
	 * @see org.springframework.web.util.Log4jConfigListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		installJulToSlf4jBridge();
		super.contextInitialized(event);
	}

	/**
	 * 
	 */
	private void installJulToSlf4jBridge() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}
}
