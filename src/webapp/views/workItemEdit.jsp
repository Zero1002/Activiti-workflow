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
                <h3 class="text-themecolor m-b-0 m-t-0">项目详情</h3>
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
                    <form class="form-horizontal form-material" method="post" action="<%=basePath%>/workItem/save">
                        <div class="form-group">
                            <label class="col-md-12">Id</label>
                            <div class="col-md-12">
                                <input id="id" name="id" type="text" value="${workItem.id}"
                                       class="form-control form-control-line" readonly>
                                <%-- 执行人 --%>
                                <input id="adminId" name="adminId" type="text" value="<%=userId%>"
                                       class="form-control form-control-line" hidden>
                            </div>
                        </div>
                        <%--TODO:数组库字段没有流程key的字段--%>
                        <div class="form-group">
                            <label class="col-sm-12">所属流程</label>
                            <div class="col-sm-12">
                                <select id="flowName" name="flowName" class="form-control form-control-line">
                                    <option value="${workItem.flowName}">${workItem.flowName}</option>
                                    <option value="TestFlow">TestFlow</option>
                                    <option value="develop">develop</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">项目名</label>
                            <div class="col-md-12">
                                <input id="itemName" name="itemName" type="text" value="${workItem.itemName}"
                                       class="form-control form-control-line">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-12">其余信息</label>
                            <c:if test="${workItem.id==null}">
                                <div class="col-md-12" style="display: inline-flex" id="extro">
                                    <input id="key1" name="key1" type="text" value=""
                                           style="width: 30%;margin-left: 32px;" placeholder="Key"
                                           class="form-control form-control-line">
                                    <input id="value1" name="value1" type="text" value="" style="margin-left: 16px;"
                                           placeholder="Value"
                                           class="form-control form-control-line">
                                </div>
                                <div class="col-md-12" style="display: inline-flex" id="extro">
                                    <input id="key2" name="key2" type="text" value=""
                                           style="width: 30%;margin-left: 32px;" placeholder="Key"
                                           class="form-control form-control-line">
                                    <input id="value2" name="value2" type="text" value="" style="margin-left: 16px;"
                                           placeholder="Value"
                                           class="form-control form-control-line">
                                </div>
                                <div class="col-md-12" style="display: inline-flex" id="extro">
                                    <input id="key3" name="key3" type="text" value=""
                                           style="width: 30%;margin-left: 32px;" placeholder="Key"
                                           class="form-control form-control-line">
                                    <input id="value3" name="value3" type="text" value="" style="margin-left: 16px;"
                                           placeholder="Value"
                                           class="form-control form-control-line">
                                </div>
                            </c:if>
                            <c:if test="${workItem.id>0}">
                                <c:forEach items="${extraInfoJson}" var="item">
                                    <div class="col-md-12" style="display: inline-flex">
                                        <input type="text" value="${item.key}" style="width: 30%;margin-left: 32px;"
                                               class="form-control form-control-line">
                                        <input type="text" value="${item.value}" style="margin-left: 16px;"
                                               class="form-control form-control-line">
                                    </div>
                                </c:forEach>
                            </c:if>

                        </div>
                        <div class="form-group">
                            <div class="col-sm-12">
                                <input type="submit" class="btn btn-success"/>
                            </div>
                        </div>

                    </form>
                    <c:if test="${workItem.id>0&&workItem.state==null}">
                        <%-- TODO:选择框指定哪个流程启动--%>
                        <a href="javascript:start();" class="btn btn-danger">启动流程</a>
                    </c:if>
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
                comment: '流程启动',
                flowKey: $('#flowName').val()
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
