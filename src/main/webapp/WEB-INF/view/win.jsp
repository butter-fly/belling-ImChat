<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
	String path = request.getContextPath();
%>
<html>
<head>
	<title>群聊-网页演示版</title>
	<jsp:include page="include/resource.jsp"/>
	<link href="${ctx}/static/plugins/face/css/smohan.face.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript">var WEB_URL = '<%=path%>';</script>
	<script type="text/javascript" src="${ctx}/static/plugins/face/js/smohan.face.js" charset="utf-8"></script>
	<script src="${ctx}/static/plugins/sockjs/sockjs.js"></script>
	<script src="${ctx}/static/plugins/hotkeys/jquery.hotkeys.js"></script>
</head>
<body>
<div class="am-cf admin-main">
	<jsp:include page="include/leftbar.jsp"/>
	<!-- content start -->
	<div class="admin-content">
		<div class="" style="width: 65%;float:left;">
			<!-- 聊天区 -->
			<div class="am-scrollable-vertical" id="chat-view" style="height:50%;">
				<!-- 消息列表 -->
				<ul class="am-comments-list am-comments-list-flip" id="chat"></ul>
			</div>
			
			<!-- 输入区 -->
			<div class="am-form-group am-form" id="Smohan_FaceBox">
				<textarea class="smohan_text" id="message" name="message" rows="4"  placeholder="正在努力输入中..." style="resize:none;"></textarea>
				<p><a href="javascript:void(0)" class="face" title="表情"></a></p>
				<!--解析表情-->
				<div id="Zones"></div>
			</div>
			
			<!-- 接收者 -->
			<div class="" style="float: left">
				<p class="am-kai">&nbsp;@ : <span id="sendto">全体成员</span>&nbsp;&nbsp;&nbsp;&nbsp;<button class="am-btn am-btn-xs am-btn-fail" onclick="resetSend2All();">恢复默认（Ctrl+r）</button></p>
			</div>
			
			<!-- 操作按钮区 -->
			<div class="am-btn-group am-btn-group-xs" style="float:right;">
				<button class="am-btn am-btn-default" type="button" onclick="clearConsole()"><span class="am-icon-paint-brush"></span> 清屏(Ctr+c)</button>
				<button class="am-btn am-btn-default" type="button" onclick="sendMessage()"><span class="am-icon-send"></span> 发送 (+Enter)</button>
			</div>
		</div>
		
		<!-- 在线用户列表区 -->
		<div class="am-panel am-panel-default" style="float:right;width: 35%;">
			<div class="am-panel-hd">
				<h3 class="am-panel-title">在线聊天室  <span id="onlinenum" class="am-badge am-badge-warning am-radius" style="border: 50%;"></span> </h3>
			</div>
			<ul class="am-list am-list-static am-list-striped" id="list"></ul>
		</div>
	</div>
	<!-- content end -->
</div>
<script type="text/javascript">
 
	$(function () {
		
		// 页面快捷键监听
		// 发送事件
		$(document).bind("keydown", "Ctrl+c", function (ev) { 
			clearConsole();//清空控制台
			return false;
		});
		// 恢复默认事件
		$(document).bind("keydown", "Ctrl+r", function (ev) { 
			resetSend2All(); // 恢复默认
			return false;
		});
		// 回车事件
		$(document).bind("keydown", "return", function (ev) { 
			sendMessage();//发送消息
			return false;
		});
		
		// 解决输入框内回车与消息发送快捷键回车冲突
		var text = document.getElementById('message');
		$(text).bind("keydown", "Ctrl+c", function (ev) { 
			clearConsole();//发送消息
			return false;
		});
		
		$(text).bind("keydown", "Ctrl+r", function (ev) { 
			resetSend2All(); // 恢复默认
			return false;
		});
		$(text).bind("keydown", "return", function (ev) { 
			sendMessage();//发送消息
			return false;
		});

		// 回车监听事件
		//document.onkeydown = keyDownSearch;

		// 初始化表情包
		$("a.face").smohanfacebox({
			Event : "click", //触发事件	
			divid : "Smohan_FaceBox", //外层DIV ID
			textid : "message" //文本框 ID
		});

		// 初始化聊天群
		$("#message").attr("placeholder", "与全体成员已建立聊天通道,等待输入中...");
	});

	// 提示信息
	if ("${message}") {
		layer.msg('${message}', {
			offset : 0
		});
	}

	// 错误提示
	if ("${error}") {
		layer.msg('${error}', {
			offset : 0,
			shift : 6
		});
	}
	var wsServer = null;
	var ws = null;
	wsServer = "ws://" + location.host + "${pageContext.request.contextPath}" + "/chatServer";
	//创建WebSocket对象
	if ('WebSocket' in window) {
		ws = new WebSocket(wsServer);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(wsServer);
	} else {
		ws = new SockJS("http://" + location.host + "${pageContext.request.contextPath}" + "/chatServer");
	}

	// Socket创建并连接
	ws.onopen = function(evt) {
		return;
		layer.msg("已建立Socket连接", {
			offset : 0
		});
	};

	// Socket 消息传递监听
	ws.onmessage = function(evt) {
		analysisMessage(evt.data); //解析后台传回的消息,并予以展示
	};

	// Socket异常监听
	ws.onerror = function(evt) {
		layer.msg("产生Socket异常", {
			offset : 0
		});
	};

	// Socket关闭监听
	ws.onclose = function(evt) {
		return;
		layer.msg("已关闭Socket连接", {
			offset : 0
		});
	};
	
	// 恢复重置
	function resetSend2All() {
		$('#sendto').text('全体成员');
		$('#message').attr('placeholder', '与全体成员已建立聊天通道,等待输入中...');
	}

	// 回车事件
	function keyDownSearch(e) {
		// 兼容FF和IE和Opera    
		var theEvent = e || window.event;
		var keyCode = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (keyCode == 13) {
			sendMessage();//发送消息
			return false;
		}
		e.preventDefault();
		return true;
	}

	/**
	 * 获取当前连接，没有在
	 */
	function getConnection() {
		if (ws == null) {
			//创建WebSocket对象
			if ('WebSocket' in window) {
				ws = new WebSocket(wsServer);
			} else if ('MozWebSocket' in window) {
				ws = new MozWebSocket(wsServer);
			} else {
				ws = new SockJS("http://" + location.host
						+ "${pageContext.request.contextPath}" + "/chatServer");
			}

			// Socket创建并连接
			ws.onopen = function(evt) {
				return;
				layer.msg("已建立Socket连接", {
					offset : 0
				});
			};

			// Socket 消息传递监听
			ws.onmessage = function(evt) {
				analysisMessage(evt.data); //解析后台传回的消息,并予以展示
			};

			// Socket异常监听
			ws.onerror = function(evt) {
				layer.msg("产生Socket异常", {
					offset : 0
				});
			};

			// Socket关闭监听
			ws.onclose = function(evt) {
				return;
				layer.msg("已关闭Socket连接", {
					offset : 0
				});
			};
		} else {
			layer.msg("Socket连接已存在!", {
				offset : 0,
				shift : 6
			});
		}
	}

	/**
	 * 关闭连接
	 */
	function closeConnection() {
		if (ws != null) {
			ws.close();
			ws = null;
			$("#list").html(""); //清空在线列表
			layer.msg("已关闭Socket连接", {
				offset : 0
			});
		} else {
			layer.msg("未开启Socket连接", {
				offset : 0,
				shift : 6
			});
		}
	}

	/**
	 * 检查连接
	 */
	function checkConnection() {
		if (ws != null) {
			layer.msg(ws.readyState == 0 ? "连接异常" : "连接正常", {
				offset : 0
			});
		} else {
			layer.msg("连接未开启!", {
				offset : 0,
				shift : 6
			});
		}
	}

	/**
	 * 发送信息给后台
	 */
	function sendMessage() {
		if (ws == null) {
			layer.msg("未开启Socket连接!", {
				offset : 0,
				shift : 6
			});
			return;
		}
		var message = $("#message").val();
		var to = $("#sendto").text() == "全体成员" ? "" : $("#sendto").text();
		if (message == null || message == "") {
			layer.msg("发送内容不能为空!", {
				offset : 0,
				shift : 6
			});
			return;
		}

		// 消息类型
		var msgType = "message";
		if ("" == to || null == to) {
			//msgType = "notice";
		}

		// 客户端向服务端发送消息
		ws.send(JSON.stringify({
			message : {
				content : message,
				from : '${userid}',
				to : to, //接收人,如果没有则置空,如果有多个接收人则用,分隔
				time : getDateFull()
			},
			type : msgType
		}));
	}

	/**
	 * 解析后台传来的消息，具体格式如下：
	 * "massage" : {
	 *              "from" : "xxx",
	 *              "to" : "xxx",
	 *              "content" : "xxx",
	 *              "time" : "xxxx.xx.xx",
	 *				"ip":"192.168.1.122"
	 *          },
	 * "type" : {notice|message},
	 * "list" : {[xx],[xx],[xx]}
	 */
	function analysisMessage(message) {
		message = JSON.parse(message);

		//会话消息
		if (message.type == "message") {
			showChat(message.message);
		}

		//提示消息
		if (message.type == "notice") {
			showNotice(message.message);
		}

		// 在线用户
		if (message.list != null && message.list != undefined) { //在线列表
			showOnline(message.list);
			resetSendTo(message.list);
		}
	}

	/**
	 * 展示提示信息-广播
	 */
	function showNotice(notice) {
		$("#chat").append("<div><p class=\"am-text-primary\" style=\"text-align:center\"><span class=\"am-icon-bullhorn\"></span>&nbsp;&nbsp;" + notice + "</p></div>");
		var chat = $("#chat-view");
		chat.scrollTop(chat[0].scrollHeight); //让聊天区始终滚动到最下面
	}

	/**
	 * 展示会话信息
	 */
	function showChat(message) {
		// alert("接受到数据：" + message.content);/
		var to = message.to == null || message.to == "" ? "全体成员" : message.to; //获取接收人
		var isSef = '${userid}' == message.from ? "am-comment-flip" : ""; //如果是自己则显示在右边,他人信息显示在左边
		var isMyselef = '${userid}' == message.from ? "我" : message.from;
		var isTO = '${userid}' == message.to ? "我" : message.to;
		if (isTO == null || '' == isTO) {
			isTO = "全体成员";
		}
		var html = "<li class=\"am-comment " + isSef + " am-comment-primary\"><a href=\"#link-to-user-home\"><img width=\"48\" height=\"48\" class=\"am-comment-avatar\" alt=\"\" src=\"http://img.mukewang.com/539ff88a0001210601400140-80-80.jpg?"
				+ message.from
				+ "/head\"></a><div class=\"am-comment-main\">\n"
				+ "<header class=\"am-comment-hd\"><div class=\"am-comment-meta\">   <a class=\"am-comment-author\" href=\"#link-to-user\">"
				+ isMyselef
				+ "</a>  <time> "
				+ message.time
				+ "</time> 发送给 -> "
				+ isTO
				+ " </div></header><div class=\"am-comment-bd\"> <p>"
				+ message.content + "</p></div></div></li>";
		$("#chat").append(tranFaces(html));
		$("#message").val(""); //清空输入区
		
		if (isMyselef != "我") {
			$("#sendto").text(message.from);
			$("#message").attr("placeholder", '与' + message.from + "已建立聊天通道,等待输入中...");
		}
		
		if (isTO == "全体成员") {
			$("#sendto").text('全体成员');
			$("#message").attr("placeholder", '与全体成员已建立聊天通道,等待输入中...');
		}

		var chat = $("#chat-view");
		chat.scrollTop(chat[0].scrollHeight); //让聊天区始终滚动到最下面
	}

	// 表情替换
	function tranFaces(faces) {
		for (i = 0; i < 60; i++) {
			faces = faces.replace('<emt>' + (i + 1) + '</emt>', '<img src="'
					+ WEB_URL + '/static/plugins/face/images/face/' + (i + 1)
					+ '.gif">');
		}
		return faces;
	}

	/**
	 * 展示在线列表
	 */
	function showOnline(list) {
		$("#list").html(""); //清空在线列表
		$.each(list,function(index, item) { //添加私聊按钮
			var li = "<li>" + item + "</li>";
			if ('${userid}' != item) { //排除自己
				li = "<li>"
					+ item
					+ " <button type=\"button\" class=\"am-btn am-btn-xs am-btn-link\" onclick=\"addChat('"
					+ item
					+ "');\"><span class=\"am-icon-comment-o\"><span> 私聊</button>"
					+ "</li>";
			}
			$("#list").append(li);
		});

		//获取在线人数
		$("#onlinenum").text("+" + $("#list li").length);
	}

	// 重置@人聊天
	function resetSendTo(list) {
		var to = $("#sendto").text() == "全体成员" ? "" : $("#sendto").text();
		if (to != "") {
			var tempArray = new Array();
			var users = to.split(",");
			for ( var u in users) {
				// 用户会话存在
				if (list.toString().indexOf(u) > -1) {
					tempArray.push(u);
				}
			}
			if (tempArray.length == 0) {
				$("#message").attr("placeholder", "与全体成员已建立聊天通道,等待输入中...");
			} else {
				$("#sendto").text('');
				sendto.text(tempArray.join(','));
				$("#message").attr("placeholder", '与' + tempArray.join(',') + "已建立聊天通道,等待输入中...");
			}
		}
	}

	/**
	 * 添加接收人
	 */
	function addChat(user) {
		var sendto = $("#sendto");
		var receive = sendto.text() == "全体成员" ? "" : sendto.text() + ",";
		if (receive.indexOf(user) == -1) { //排除重复
			sendto.text(receive + user);
			$("#message").attr("placeholder", '与' + receive + user + "已建立聊天通道,等待输入中...");
		}
	}

	/**
	 * 清空聊天区
	 */
	function clearConsole() {
		$("#chat").html("");
	}

	function appendZero(s) {
		return ("00" + s).substr((s + "").length);
	} //补0函数

	// 日期格式化函数
	function getDateFull() {
		var date = new Date();
		var currentdate = date.getFullYear() + "-"
				+ appendZero(date.getMonth() + 1) + "-"
				+ appendZero(date.getDate()) + " "
				+ appendZero(date.getHours()) + ":"
				+ appendZero(date.getMinutes()) + ":"
				+ appendZero(date.getSeconds());
		return currentdate;
	}

	// 退出群聊
	function logOut() {
		closeConnection();
		window.parent.parent.location.href = '${pageContext.request.contextPath}/user/logout';
	}
</script>
</body>
</html>
