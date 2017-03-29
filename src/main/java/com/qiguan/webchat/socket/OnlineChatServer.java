package com.qiguan.webchat.socket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**  
 * <pre>
 * Description	WebSocket服务-定义成一个WebSocket服务器端，用于监听用户连接的终端访问URL地址
 * Copyright:	Copyright (c)2017
 * Company:		杭州启冠网络技术有限公司
 * Author:		Administrator
 * Version: 	1.0
 * Create at:	2017年3月27日 下午4:42:36  
 *  
 * Modification History:  
 * Date         Author      Version     Description 
 * ------------------------------------------------------------------  
 * 
 * </pre>
 */  
@ServerEndpoint(value = "/chatServer", configurator = HttpChatSessionConfigurator.class)
public class OnlineChatServer {
	
	/**
	 * 静态变量，用来记录当前在线连接数
	 */
	private static volatile int onlineCount = 0;
	
	/**
	 * 当前会话
	 */
	private static CopyOnWriteArraySet<OnlineChatServer> webSocketSet = Sets.newCopyOnWriteArraySet();
	
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;
	
	/**
	 * 用户名
	 */
	private String userid; 
	
	/**
	 * request的session
	 */
	private HttpSession httpSession; 

	/**
	 * 在线列表,记录用户名称
	 */
	private static List<String> OnlineUserlist = Lists.newCopyOnWriteArrayList();
	
	/**
	 *  用户名和websocket的session绑定的路由表
	 */
	private static Map<String, Session> routetabMap = Maps.newConcurrentMap();

	/**
	 * 连接建立成功调用的方法-与前端JS代码对应
	 * 
	 * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		// 单个会话对象保存
		this.session = session;
		webSocketSet.add(this); // 加入set中
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		this.userid = (String) httpSession.getAttribute("userid"); // 获取当前用户
		if (!OnlineUserlist.contains(this.userid)) {
			OnlineUserlist.add(userid); // 将用户名加入在线列表
			addOnlineCount(); // 在线数加1;
		}
		routetabMap.put(userid, session); // 将用户名和session绑定到路由表
		String message = getMessage(userid + " -> 已上线", "notice", OnlineUserlist);
		broadcast(message); // 广播
	}

	/**
	 * 连接关闭调用的方法-与前端JS代码对应
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		OnlineUserlist.remove(userid); // 从在线列表移除这个用户
		routetabMap.remove(userid);
		String message = getMessage(userid + " -> 已下线", "notice", OnlineUserlist);
		broadcast(message); // 广播
	}

	/**
	 * 接收客户端的message,判断是否有接收人而选择进行广播还是指定发送 -与前端JS代码对应
	 * 消息格式JSON，参数如下：
	 * "massage" : { "from" : "xxx", "to" : "xxx", "content" : "xxx", "time" :
	 * "xxxx.xx.xx" }, 
	 * "type" : {notice|message},
	 *  "list" : {[xx],[xx],[xx]}
	 *  
	 * @param _message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String _message) {
		JSONObject chat = JSON.parseObject(_message);
		JSONObject message = JSON.parseObject(chat.get("message").toString());
		// 如果to为空,则广播;如果不为空,则对指定的用户发送消息 - 广播
		if (message.get("to") == null || message.get("to").equals("")) { 
			broadcast(_message);
		} else {
			// 可以发送给多个用户
			String[] userlist = message.get("to").toString().split(",");
			// 先发送给自己,重要！！！
			singleSend(_message, (Session) routetabMap.get(message.get("from")));
			// 遍历要发送的用户列表
			for (String user : userlist) {
				// 不要重复发送给自己
				if (!user.equals(message.get("from"))) {
					// 获取用户会话Session
					singleSend(_message, (Session) routetabMap.get(user)); // 分别发送给每个指定用户
				}
			}
		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param error
	 */
	@OnError
	public void onError(Throwable error) {
		System.out.println(error.getLocalizedMessage());
	}

	/**
	 * 广播消息
	 * 
	 * @param message
	 */
	public void broadcast(String message) {
		// 判断当前会话人数
		if (webSocketSet.size() == 0) return;
		// 获取当前所有的会话session
		for (OnlineChatServer chat : webSocketSet) {
			chat.session.getAsyncRemote().sendText(message);
		}
	}

	/**
	 * 对特定用户发送消息
	 * 
	 * @param message
	 * @param session
	 */
	public void singleSend(String message, Session session) {
		session.getAsyncRemote().sendText(message); // 异步发送
	}

	/**
	 * 组装返回给前台的消息
	 * 
	 * @param message 交互信息
	 * @param type 信息类型
	 * @param list 在线列表
	 * @return
	 */
	public String getMessage(String message, String type, List<String> list) {
		JSONObject member = new JSONObject();
		member.put("message", message);
		member.put("type", type);
		member.put("list", list);
		return member.toString();
	}

	/**
	 * 统计在线人数
	 * 
	 * @return
	 */
	public synchronized int getOnlineCount() {
		return onlineCount;
	}

	/**
	 * 在线人数++
	 */
	public synchronized void addOnlineCount() {
		OnlineChatServer.onlineCount++;
	}

	/**
	 * 在线人数--
	 */
	public synchronized void subOnlineCount() {
		OnlineChatServer.onlineCount--;
	}
}
