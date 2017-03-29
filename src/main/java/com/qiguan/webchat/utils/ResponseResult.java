package com.qiguan.webchat.utils;

import java.util.HashMap;

/**  
 * <pre>
 * Description	 公共返回类
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:55:43  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
public class ResponseResult extends HashMap<String, Object> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4417307908728831525L;
	public static final int SUCCESS = 0;
	public static final int ERROR = -1;

	public ResponseResult(int _result, String _msg) {
		this.put("result", _result);
		this.put("msg", _msg);
	}

	public static ResponseResult success(String _msg) {
		return new ResponseResult(SUCCESS, _msg);
	}

	public static ResponseResult error(String _msg) {
		return new ResponseResult(ERROR, _msg);
	}
}
