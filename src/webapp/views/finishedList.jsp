<%@ page import="static org.activiti.engine.impl.util.json.Cookie.unescape" %><%--
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
        </div>
        <div class="row">
            <!-- column -->
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-block">
                        <div class="table-responsive">
                            <table id="table" class="table" style="text-align:center">
                                <thead>
                                <tr>
                                    <th style="white-space: nowrap;text-align:center">任务Id</th>
                                    <th style="white-space: nowrap;text-align:center">项目id</th>
                                    <th style="white-space: nowrap;text-align:center">任务节点</th>
                                    <th style="white-space: nowrap;text-align:center">处理人</th>
                                    <th style="white-space: nowrap;text-align:center">描述</th>
                                    <th style="white-space: nowrap;text-align:center">创建时间</th>
                                    <th style="white-space: nowrap;text-align:center">结束时间</th>
                                    <th style="white-space: nowrap;text-align:center">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${taskList}" var="item">
                                    <tr>
                                        <td>${item.taskId}</td>
                                        <td><a href="<%=basePath%>/workItem/${item.id}">${item.id}</a></td>
                                        <td>${item.taskName}</td>
                                        <td>${item.currentHandleName}</td>
                                        <td>${item.description}</td>
                                        <td><fmt:formatDate value='${item.createTime}' pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                        <td><fmt:formatDate value='${item.endTime}' pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                        <td>
                                            <a href="<%=basePath%>/testFlow/showCurrentView?processInstanceId=${item.processInstanceId}"
                                               class="btn btn-info">查看流程图</a>
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


<script type="text/javascript">

</script>
</body>


</html>
