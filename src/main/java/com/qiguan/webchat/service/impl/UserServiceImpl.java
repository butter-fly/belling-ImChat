package com.qiguan.webchat.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiguan.webchat.dao.IUserDao;
import com.qiguan.webchat.entity.User;
import com.qiguan.webchat.service.IUserService;

/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午5:01:47  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@Service(value = "userService")
public class UserServiceImpl implements IUserService {

	/**
	 * 
	 */
	@Resource
	private IUserDao userDao;

	/* (non-Javadoc)
	 * @see com.qiguan.webchat.service.IUserService#selectUserByUserid(java.lang.String)
	 */
	@Override
	public User selectUserByUserid(String userid) {
		return userDao.selectUserByUserid(userid);
	}
}
