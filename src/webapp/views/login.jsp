<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/12
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String message = (String) request.getAttribute("message");
%>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>登陆</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/auth.css">
</head>

<body onload="checkBody();">
<div class="lowin">
    <div class="lowin-wrapper">
        <div class="lowin-box lowin-login">
            <div class="lowin-box-inner">

                <form id="fm" name="fm" action="<%=basePath%>/user/login" method="post" onsubmit="return check();">
                    <p style="font-size:32px;">登陆</p>
                    <div class="lowin-group">
                        <label>手机号</label>
                        <input style="color: dimgray;" type="text" name="phone" id="phone" class="lowin-input">
                    </div>
                    <div class="lowin-group password-group">
                        <label>密码</label>
                        <input style="color: dimgray;" type="password" name="password" id="password"
                               autocomplete="current-password"
                               class="lowin-input">
                    </div>
                    <input type="submit" id="login" class="lowin-btn login-btn" value="登陆"/>
                    <%-- 错误返回 --%>
                    <div id="messagePanel" style="display:none" class="text-foot">
                        <label id="message" style="color: red;font-weight: bolder;"><%=message %>
                        </label>
                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<script src="/resources/js/auth.js"></script>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

<script type="text/javascript">
    // 重载页面
    function checkBody() {
        var message = '<%=message%>';
        if (message != 'null') {
            document.getElementById("messagePanel").style.display = "";//messagePanel
        }
    }

    // 提交表单前检查
    function check() {
        var frm = document.fm;
        if (frm.loginName.value == "") {
            alert("用户名不能为空!");
            document.fm.loginName.focus();
            return false;
        } else if (frm.password.value == "") {
            alert("登录密码不能为空!");
            document.fm.password.focus();
            return false;
        } else {
            return true;
        }
    }
</script>
</body>
</html>
