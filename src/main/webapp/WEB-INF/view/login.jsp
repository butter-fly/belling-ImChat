<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>群聊-网页演示版</title>
<link href="<%=path%>/static/source/css/login.css" rel='stylesheet' type='text/css' />
<script src="<%=path%>/static/plugins/jquery/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/static/plugins/layer/layer.js"></script>
</head>
<body>
	<h1>群聊-网页演示版</h1>
	<div class="login-form">
		<div class="clear"></div>
		<form id="login-form" action="<%=path%>/user/login" method="post"
			onsubmit="return checkLoginForm()">
			<div class="key">
				<input type="text" id="username" name="userid" placeholder="账号">
			</div>
			<div class="key">
				<input type="password" id="password" name="password"
					placeholder="密码">
			</div>
			<div class="signin">
				<input type="submit" id="submit" value="登 录">
			</div>
		</form>
	</div>

	<script type="text/javascript">
		$(function() {
			<c:if test="${not empty param.timeout}">
			layer.msg('登录会话超时,请重新登陆吆~', {
				offset : 0,
				shift : 6
			});
			</c:if>

			if ("${error}") {
				layer.msg('${error}', {
					offset : 0,
				});
			}

			if ("${message}") {
				layer.msg('${message}', {
					offset : 0,
				});
			}
		});
		
		// 校验表单
		function checkLoginForm() {
			var username = $('#username').val();
			var password = $('#password').val();
			if (isNull(username) && isNull(password)) {
				layer.msg('请输入账号和密码~', {
					offset : 0,
				});
				return false;
			}
			if (isNull(username)) {
				layer.msg('请输入账号~', {
					offset : 0,
				});
				return false;
			}
			if (isNull(password)) {
				layer.msg('请输入密码~', {
					offset : 0,
				});
				return false;
			} else {
				$('#submit').attr('value', '正在登录中~');
				return true;
			}
		}

		function isNull(input) {
			if (input == null || input == '' || input == undefined) {
				return true;
			} else {
				return false;
			}
		}
	</script>
</body>
</html>