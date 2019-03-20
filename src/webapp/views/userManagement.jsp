<%@ page import="com.springmvc.pojo.User" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/18
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- fmt需加/jsp前缀，否则500 --%>
<%-- 页面解析EL表达式需加isELIgnored="false" --%>

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
<body class="fix-header fix-sidebar card-no-border">
<div class="page-wrapper">
    <div class="container-fluid">
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">用户管理</h3>
            </div>
            <div class="col-md-7 col-4 align-self-center">
                <a href="#" class="btn waves-effect waves-light btn-success pull-right hidden-sm-down">新增</a>
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
                                    <th>id</th>
                                    <th>用户名</th>
                                    <th>角色名</th>
                                    <th>更新时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${userlists}" var="item">
                                    <tr>
                                        <td>${item.id}</td>
                                        <td>${item.loginName}</td>
                                        <td>${item.roleName}</td>
                                        <td><fmt:formatDate value='${item.updatedAt}'
                                                            pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                        <td>
                                            <a href="./userEdit.jsp" class="btn btn-warning">编辑</a>
                                            <a href="#" class="btn btn-danger">删除</a>
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
</body>


</html>
