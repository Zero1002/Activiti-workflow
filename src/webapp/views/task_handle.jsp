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
    Integer userId = (Integer) session.getAttribute("SESSION_ID");
    Integer roleId = (Integer) session.getAttribute("SESSION_ROLE_ID");
%>
<html>
<head>
    <jsp:include page="./baseView.jsp"></jsp:include>
    <title>流程审批</title>
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
                <h3 class="text-themecolor m-b-0 m-t-0">任务详情</h3>
            </div>
            <%-- 按钮 --%>
            <%--<div class="col-md-7 col-4 align-self-center">--%>
            <%--<a href="#" class="btn waves-effect waves-light btn-danger pull-right hidden-sm-down"> Upgrade to--%>
            <%--Pro</a>--%>
            <%--</div>--%>
        </div>
        <div class="row">
            <div class="col-lg-4 col-xlg-3 col-md-5">
                <div class="card">
                    <div class="card-block">
                        <form class="form-horizontal form-material" method="post" action="<%=basePath%>/workItem/save">
                            <div class="form-group">
                                <label class="col-md-12">项目Id</label>
                                <div class="col-md-12">
                                    <input id="id" name="id" type="text" value="${workItem.id}"
                                           class="form-control form-control-line" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-12">项目名</label>
                                <div class="col-md-12">
                                    <input id="flowName" name="flowName" type="text" value="${workItem.flowName}"
                                           class="form-control form-control-line">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-12">其余信息</label>
                                <c:forEach items="${extraInfoJson}" var="item">
                                    <div class="col-md-12" style="display: inline-flex">
                                        <input type="text" value="${item.key}" style="width: 30%;margin-left: 32px;"
                                               class="form-control form-control-line">
                                        <input type="text" value="${item.value}" style="margin-left: 16px;"
                                               class="form-control form-control-line">
                                    </div>
                                </c:forEach>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <%--  右侧小卡片 --%>
            <div class="col-lg-8 col-xlg-9 col-md-7">
                <div class="card">
                    <div class="card-block">
                        <table id="table" class="table" style="text-align:center">
                            <thead>
                            <tr>
                                <th style="white-space: nowrap;text-align:center">taskId</th>
                                <th style="white-space: nowrap;text-align:center">节点名称</th>
                                <th style="white-space: nowrap;text-align:center">描述</th>
                                <th style="white-space: nowrap;text-align:center">创建时间</th>
                                <th style="white-space: nowrap;text-align:center">预计时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>${task.id}</td>
                                <td>${task.name}</td>
                                <td>${task.description}</td>
                                <td><fmt:formatDate value='${task.createTime}'
                                                    pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                <td><fmt:formatDate value='${task.dueDate}'
                                                    pattern='yyyy-MM-dd HH:ss:mm'/></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script type="text/javascript">
    // 启动流程
    function start() {
        if (${workItem.id>0}) {
            $.post("<%=basePath%>/testFlow/start", {
                id: ${workItem.id},
                userId:<%=userId%>,
                operation: '提交',
                comment: '测试流程提交，后续提交评论'
            }, function (result) {
                if (result.data.success) {
                    alert("启动流程成功");
                } else {
                    alert(result.data.errorMsg);
                }
            }, "json");
        } else {
            alert("请先保存当前项目");
        }
    }
</script>
</body>
</html>
