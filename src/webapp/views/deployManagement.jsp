<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/5/2
  Time: 10:40
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
    <title>流程部署管理</title>
</head>
<body>

<div class="page-wrapper">
    <div class="container-fluid">
        <div class="row page-titles">
            <div class="col-md-5 col-8 align-self-center">
                <h3 class="text-themecolor m-b-0 m-t-0">流程部署管理</h3>
            </div>
            <div class="col-md-7 col-4 align-self-center">
                <%-- 文件上传 --%>
                <form id="fm" action="" method="post" enctype="multipart/form-data">
                    <input type="file" class="btn" id="deployFile" name="deployFile" required="true"/>
                    <a href="javascript:saveDeploy()" class="btn btn-success">部署流程图</a>
                </form>
            </div>
        </div>
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <div class="table-responsive">
                        <table id="table" class="table" style="text-align:center">
                            <thead>
                            <tr>
                                <th style="white-space: nowrap;text-align:center">编号</th>
                                <th style="white-space: nowrap;text-align:center">流程名称</th>
                                <th style="white-space: nowrap;text-align:center">部署时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${deployLists}" var="item">
                                <tr>
                                    <td>${item.id}</td>
                                    <td>${item.name}</td>
                                    <td>${item.deploymentTime}</td>
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

<script type="text/javascript" src=/resources/js/jquery-easyui-1.3.3/jquery.easyui.min.js></script>
<script type="text/javascript">
    // 部署上传
    function saveDeploy() {
        var isConfirm = confirm("确定部署吗");
        if (isConfirm) {
            $("#fm").form("submit", {
                url: "<%=basePath%>/deploy/deploy",
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    if (result.data.success) {
                        alert("部署成功");
                        location.reload();
                    } else {
                        alert(result.data.errorMsg);
                    }
                }
            });
        }
    }
</script>
</body>
</html>

