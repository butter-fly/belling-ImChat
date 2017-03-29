package com.qiguan.webchat.utils;

import java.util.UUID;

/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:55:37  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public class StringUtil {
	public static String getGuid() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
	}
}
