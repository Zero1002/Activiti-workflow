<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/12
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>登陆</title>
    <link rel="stylesheet" type="text/css" href="/resources/css/auth.css">
</head>

<body>
<div class="lowin">
    <div class="lowin-wrapper">
        <div class="lowin-box lowin-login">
            <div class="lowin-box-inner">
                <form>
                    <p>登陆</p>
                    <div class="lowin-group">
                        <label>用户名 <a href="#" class="login-back-link">Sign in?</a></label>
                        <input type="text" name="loginName" id="loginName" class="lowin-input">
                    </div>
                    <div class="lowin-group password-group">
                        <label>密码 <a href="#" class="forgot-link">Forgot Password?</a></label>
                        <input type="password" name="password" id="password" autocomplete="current-password"
                               class="lowin-input">
                    </div>
                    <button id="login" class="lowin-btn login-btn">
                        登陆
                    </button>

                    <div class="text-foot">
                        没有账号? <a href="" class="register-link">注册</a>
                    </div>
                </form>
            </div>
        </div>

        <div class="lowin-box lowin-register" hidden="true">
            <div class="lowin-box-inner">
                <form>
                    <p>创建账号</p>
                    <div class="lowin-group">
                        <label>姓名</label>
                        <input type="text" name="name" autocomplete="name" class="lowin-input">
                    </div>
                    <div class="lowin-group">
                        <label>邮箱</label>
                        <input type="email" autocomplete="email" name="email" class="lowin-input">
                    </div>
                    <div class="lowin-group">
                        <label>密码</label>
                        <input type="password" name="password" autocomplete="current-password" class="lowin-input">
                    </div>
                    <button class="lowin-btn">
                        注册
                    </button>

                    <div class="text-foot">
                        已有账户? <a href="javascript:changeView()" class="login-link">登陆</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="/resources/js/auth.js"></script>
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script>
    $(function () {
        $('#login').click(function () {
            var loginName = $('#loginName').val();
            var pwd = $('#password').val();
            $.ajax({
                type: "POST",
                url: "page.php",
                dataType: 'json',
                data: {
                    loginName: loginName,
                    pwd: pwd
                },
                success: function (data) {
                    console.log("返回的数据: " + data);
                }
            });
        });
    });
</script>
</body>
</html>
