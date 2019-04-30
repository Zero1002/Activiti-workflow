<%@ page import="com.springmvc.pojo.User" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/4/21
  Time: 10:23
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
    <title>后台管理系统首页</title>
</head>
<body class="fix-header fix-sidebar card-no-border">
<div class="page-wrapper">
    <div class="container-fluid">
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">流程图</h3>
            </div>
        </div>
        <div class="row">
            <!-- column -->
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-block">
                        <div class="table-responsive">
                            <div style="overflow-x: auto;white-space: nowrap;">
                                <img style="top: 0px;left: 0px;width: max-content;height: max-content;"
                                     src="<%=basePath%>/testFlow/showView?deploymentId=${deploymentId}&diagramResourceName=${diagramResourceName}"/>
                                <%--<div style="position: absolute;border:1px solid red;top:${y-3 }px;left: ${x-3 }px;width: ${width+3 }px;height:${height+3 }px;"></div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%--流程扭转过程--%>
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-block">
                        <div class="table-responsive">
                            <table id="table" class="table" style="text-align:center">
                                <thead>
                                <tr>
                                    <th style="white-space: nowrap;text-align:center">任务Id</th>
                                    <th style="white-space: nowrap;text-align:center">项目id</th>
                                    <th style="white-space: nowrap;text-align:center">节点名称</th>
                                    <th style="white-space: nowrap;text-align:center">处理人</th>
                                    <th style="white-space: nowrap;text-align:center">操作</th>
                                    <th style="white-space: nowrap;text-align:center">描述</th>
                                    <th style="white-space: nowrap;text-align:center">创建时间</th>
                                    <th style="white-space: nowrap;text-align:center">处理时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${taskList}" var="item">
                                    <c:if test="${item.endTime==null||item.endTime==''}">
                                        <tr style="background-color: yellow;color: black;">
                                    </c:if>
                                    <c:if test="${item.endTime!=null&&item.endTime!=''}">
                                        <tr>
                                    </c:if>
                                    <td>${item.taskId}</td>
                                    <td><a href="<%=basePath%>/workItem/${item.id}">${item.id}</a></td>
                                    <td>${item.taskName}</td>
                                    <td>${item.currentHandleName}</td>
                                    <td>${item.operation}</td>
                                    <td>${item.description}</td>
                                    <td>${item.createTime}</td>
                                    <td>${item.endTime}</td>
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
