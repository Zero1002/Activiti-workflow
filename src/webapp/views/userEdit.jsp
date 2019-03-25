<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/20
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<html>
<head>
    <jsp:include page="./baseView.jsp"></jsp:include>
    <title>编辑</title>
</head>
<body>
<div class="page-wrapper">
    <!-- ============================================================== -->
    <!-- Container fluid  -->
    <!-- ============================================================== -->
    <div class="container-fluid">
        <!-- ============================================================== -->
        <!-- Bread crumb and right sidebar toggle -->
        <!-- ============================================================== -->
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">用户编辑</h3>
            </div>
            <%-- 按钮 --%>
            <%--<div class="col-md-7 col-4 align-self-center">--%>
            <%--<a href="#" class="btn waves-effect waves-light btn-danger pull-right hidden-sm-down"> Upgrade to--%>
            <%--Pro</a>--%>
            <%--</div>--%>
        </div>
        <div class="col-lg-8 col-xlg-9 col-md-7">
            <div class="card">
                <div class="card-block">
                    <form class="form-horizontal form-material" method="post" action="<%=basePath%>/user/save">
                        <div class="form-group">
                            <label class="col-md-12">Id</label>
                            <div class="col-md-12">
                                <input id="id" name="id" type="text" value="${user.id}"
                                       class="form-control form-control-line" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-12">角色</label>
                            <div class="col-sm-12">
                                <select id="roleId" name="roleId" class="form-control form-control-line">
                                    <option value="${user.roleId}">${user.roleName}</option>
                                    <c:forEach items="${rolelists}" var="item">
                                        <option value="${item.id}">${item.roleName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">用户名</label>
                            <div class="col-md-12">
                                <input id="loginName" name="loginName" type="text" value="${user.loginName}"
                                       class="form-control form-control-line">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">密码</label>
                            <div class="col-md-12">
                                <input id="password" name="password" type="password" value="${user.password}"
                                       class="form-control form-control-line">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="example-email" class="col-md-12">邮箱</label>
                            <div class="col-md-12">
                                <input type="email" value="${user.email}"
                                       id="email" name="email"
                                       class="form-control form-control-line" name="example-email"
                                       id="example-email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">电话</label>
                            <div class="col-md-12">
                                <input type="text" placeholder="${user.phone}"
                                       id="phone" name="phone"
                                       class="form-control form-control-line">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <input type="submit" class="btn btn-success"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    // 保存
    function save() {
    }
</script>
</body>
</html>
