<%@ page import="com.springmvc.pojo.User" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/18
  Time: 21:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>



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
                <h3 class="text-themecolor m-b-0 m-t-0">角色管理</h3>
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
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>

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
