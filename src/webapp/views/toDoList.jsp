<%@ page import="static org.activiti.engine.impl.util.json.Cookie.unescape" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/27
  Time: 21:28
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
    <title>后台管理系统首页</title>
</head>
<body class="fix-header fix-sidebar card-no-border">
<div class="page-wrapper">
    <div class="container-fluid">
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">
                    待办任务管理
                </h3>
            </div>
            <%--<div class="col-md-7 col-4 align-self-center">--%>
            <%--<a href="#"--%>
            <%--class="btn waves-effect waves-light btn-success pull-right hidden-sm-down">没想好干嘛的按钮</a>--%>
            <%--</div>--%>
        </div>
        <div class="row">
            <!-- column -->
            <div id="taskListCard" class="col-lg-12">
                <div class="card">
                    <div class="card-block">
                        <div class="table-responsive">
                            <table id="table" class="table" style="text-align:center">
                                <thead>
                                <tr>
                                    <th style="white-space: nowrap;text-align:center">任务Id</th>
                                    <th style="white-space: nowrap;text-align:center">项目id</th>
                                    <th style="white-space: nowrap;text-align:center">项目状态</th>
                                    <th style="white-space: nowrap;text-align:center">前处理人</th>
                                    <th style="white-space: nowrap;text-align:center">现应处理人</th>
                                    <th style="white-space: nowrap;text-align:center">描述</th>
                                    <th style="white-space: nowrap;text-align:center">创建时间</th>
                                    <%--<th style="white-space: nowrap;text-align:center">预计时间</th>--%>
                                    <%--<th style="white-space: nowrap;text-align:center">结束时间</th>--%>
                                    <th style="white-space: nowrap;text-align:center">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${taskList}" var="item">
                                    <tr>
                                        <td>${item.taskId}</td>
                                        <td><a href="<%=basePath%>/workItem/${item.id}">${item.id}</a></td>
                                        <td>${item.taskName}</td>
                                        <td>${item.preHandleName}</td>
                                        <td>${item.currentHandleName}</td>
                                        <td>${item.description}</td>
                                        <td><fmt:formatDate value='${item.createTime}'
                                                            pattern='yyyy-MM-dd HH:ss:mm'/></td>
                                            <%--<td><fmt:formatDate value='${item.expectTime}' pattern='yyyy-MM-dd HH:ss:mm'/></td>--%>
                                            <%--<td><fmt:formatDate value='${item.endTime}' pattern='yyyy-MM-dd HH:ss:mm'/></td>--%>

                                        <td>
                                            <a href="javascript:showHandel(${item.taskId});" class="btn btn-warning">开始处理</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </div>
            <%--  右侧小卡片 --%>
            <div id="taskHandleCard" class="col-lg-4 col-xlg-3 col-md-5" style="display: none;">
                <div class="card">
                    <div class="card-block">
                        <div class="form-group">
                            <label class="col-md-12">任务Id</label>
                            <div class="col-md-12">
                                <input id="taskId" name="taskId" type="text" value=""
                                       class="form-control form-control-line" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">批注</label>
                            <div class="col-md-12">
                                <input id="comment" name="comment" type="text" value=""
                                       class="form-control form-control-line">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-12">指定角色</label>
                            <div class="col-sm-12">
                                <select id="assignRole" name="assignRole" class="form-control form-control-line">
                                    <option value=""></option>
                                    <c:forEach items="${rolelists}" var="item">
                                        <option value="${item.id}">${item.roleName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-12">指定用户</label>
                            <div class="col-sm-12">
                                <select id="assignUserId" name="assignUserId" class="form-control form-control-line">
                                    <option value=""></option>
                                    <c:forEach items="${userlists}" var="item">
                                        <option value="${item.id}">${item.loginName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">操作</label>
                            <div class="col-md-12">
                                <div style="display: inline-flex">
                                    <a href="javascript:showHandel();" class="btn btn-info" style="margin:8px">关闭</a>
                                    <a href="javascript:etrustOthers();" class="btn btn-info"
                                       style="margin:8px">指定其他审批人</a>
                                </div>

                                <div id="handleOperations" style="display: inline-flex">
                                    <%-- 审批按钮 --%>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<script type="text/javascript">
    var isShow = false;
    var n = 1;

    // 展现审批界面
    function showHandel($taskId) {
        isShow = !isShow;
        if (isShow) {
            if (n == 1) {
                $.post("<%=basePath%>/task/listRoleWithOperations", {
                    taskId: $taskId,
                    flowName: "develop"
                }, function (result) {
                    if (result.data.success) {
                        var operations = result.data.operations;
                        // 我简直天才啊！！！
                        for (var i in operations) {
                            document.getElementById("handleOperations")
                                .insertAdjacentHTML("afterEnd",
                                    "<a href=\"javascript:taskHandle('" + operations[i] + "');\" " +
                                    " class='btn btn-success'  style='margin:8px'>" + operations[i] + " </a>");
                        }
                    } else {
                        alert("No Right");
                    }
                }, "json");
                n++;
            }
            document.getElementById("taskListCard").className = "col-lg-8 col-xlg-9 col-md-7";
            document.getElementById("taskHandleCard").style.display = "";
            $('#taskId').val($taskId);
        } else {
            document.getElementById("taskListCard").className = "col-lg-12";
            document.getElementById("taskHandleCard").style.display = "none";
        }
    }

    // 审批处理
    function taskHandle($operation) {
        var taskId = $('#taskId').val();
        var comment = $('#comment').val();
        var isConfirm = confirm("确定当前操作[" + $operation + "]吗");
        if (isConfirm) {
            $.post("<%=basePath%>/testFlow/handle", {
                taskId: taskId,
                userId:<%=userId%>,
                operation: $operation,
                comment: comment
            }, function (result) {
                if (result.data.success) {
                    alert("操作成功");
                    location.reload();
                } else {
                    alert(result.data.errorMsg);
                }
            }, "json");
        }
    }

    // 选定角色或用户指定审批
    function etrustOthers() {
        var taskId = $('#taskId').val();
        var assignUserId = $('#assignUserId').val();
        var assignRole = $('#assignRole').val();
        if (assignUserId == '' && assignRole == '') {
            alert("请指定委托人");
            return false;
        }
        var isConfirm = confirm("确定委托处理吗");
        if (isConfirm) {
            $.post("<%=basePath%>/task/entrustHandle", {
                taskId: taskId,
                assignUserId: assignUserId,
                assignRole: assignRole
            }, function (result) {
                if (result.data.success) {
                    alert("委托成功");
                    location.reload();
                } else {
                    alert(result.data.errorMsg);
                }
            }, "json");
        }
    }
</script>
</body>


</html>
