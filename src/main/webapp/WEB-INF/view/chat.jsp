<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%String path = request.getContextPath();%>
<!DOCTYPE html>
<html>
	<head>
		<title>群聊-演示版</title>
		<script src="<%=path%>/static/plugins/jquery/jquery-2.1.4.min.js"></script>
		<script src="<%=path%>/static/plugins/layer/layer.js"></script>
	</head>
	<body >
		<script type="text/javascript">
			var _path = '<%=path%>';
			$(function() {
				var url = _path + '/win';
				<c:if test="${not empty param.timeout}">
				layer.msg('连接超时,请重新登陆!', {
					offset : 0,
					shift : 6
				});
				url = _path + '/user/login';
				</c:if>
				layer.open({
					id: 'chat-frame',
					type: 2,
					title: '群聊-网页演示版',
					shadeClose: false,
					shade: 0.2,
					closeBtn: 0, 
					maxmin: true,
					area: ['65%', '80%'],
					content: url
				});
			});
			
			//关闭浏览器提示信息
			/*
			window.onbeforeunload = function () {
				//询问框
				layer.confirm('确认要退出当前群聊？', {
					btn: ['确定','取消'] //按钮
				}, function() {
					layer.msg('已成功退出群聊', {icon: 1});
					closeWin();
				}, function(){
					
				});
			} */
			
			// 关闭窗口
			function closeWin() {
				var frameId = document.getElementById('chat-frame').getElementsByTagName("iframe")[0].id;
				$('#'+ frameId)[0].contentWindow.logOut();
			}
		</script>
	</body>
</html>