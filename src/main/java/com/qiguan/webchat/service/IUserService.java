package com.qiguan.webchat.service;

import com.qiguan.webchat.entity.User;

/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午5:01:53  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public interface IUserService {
	public User selectUserByUserid(String userid);
}
