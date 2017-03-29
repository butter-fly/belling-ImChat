package com.qiguan.webchat.dao;

import com.qiguan.webchat.entity.User;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**  
 * <pre>
 * Description
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:41:12  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@Service(value = "userDao")
public interface IUserDao {
    List<User> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    User selectUserByUserid(String userid);

    User selectCount();

    boolean insert(User user);

    boolean update(User user);

    boolean delete(String userid);
}
