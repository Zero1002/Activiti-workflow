<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/27
  Time: 21:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    String userName = (String) session.getAttribute("SESSION_NAME");
    Cookie[] cookies = request.getCookies();
    for (int i = 0; i < cookies.length - 1; i++) {
        Cookie cookie = cookies[i];
        if (cookie != null && "COOKIE_NAME".equals(cookie.getName())) {
            userName = cookie.getValue();
        }
    }
    if (userName == null || userName == "") {
%>
<div>会话失效,请重新登陆</div>
<%
        return;
    }
%>

<html>
<head>
    <jsp:include page="./baseView.jsp"></jsp:include>
    <title>后台管理系统首页</title>
</head>
<body onload="onload();">
<div class="page-wrapper">
    <div class="container-fluid">
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">
                    已办任务管理
                </h3>
            </div>
            <div class="col-md-7 col-4 align-self-center">
                <a href="#"
                   class="btn waves-effect waves-light btn-success pull-right hidden-sm-down">没想好干嘛的按钮</a>
            </div>
        </div>
        <div class="row">
            <!-- column -->
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-block">
                        <div class="table-responsive">
                            <table id="table" class="table">
                                <thead>
                                <tr>
                                    <th>taskId</th>
                                    <th>项目id</th>
                                    <th>任务名称</th>
                                    <th>前点处理人</th>
                                    <th>现应处理人</th>
                                    <th>描述</th>
                                    <th>创建时间</th>
                                    <th>结束时间</th>
                                    <th>预计处理时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${}" var="item">
                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.loginName}</td>
                                        <td>${item.roleName}</td>
                                        <td><fmt:formatDate value='${item.updatedAt}' pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                        <td>${item.id}</td>
                                        <td>${item.id}</td>
                                        <td>${item.id}</td>
                                        <td>${item.id}</td>
                                        <td>${item.id}</td>
                                        <td>
                                            <a href="#" class="btn btn-warning">开始处理</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>

<script type="text/javascript">
</script>
</body>


</html>
