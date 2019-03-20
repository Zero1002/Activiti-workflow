<%@ page import="com.springmvc.pojo.User" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/3/18
  Time: 22:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="/resources/assets/images/favicon.png">
    <!-- Bootstrap Core CSS -->
    <link href="/resources/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- chartist CSS -->
    <link href="/resources/assets/plugins/chartist-js/dist/chartist.min.css" rel="stylesheet">
    <link href="/resources/assets/plugins/chartist-js/dist/chartist-init.css" rel="stylesheet">
    <link href="/resources/assets/plugins/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.css"
          rel="stylesheet">
    <!--This page css - Morris CSS -->
    <link href="/resources/assets/plugins/c3-master/c3.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="/resources/css/style.css" rel="stylesheet">
    <!-- You can change the theme colors from here -->
    <link href="/resources/css/colors/blue.css" id="theme" rel="stylesheet">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css"
          href="/resources/js/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/resources/js/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript"
            src="/resources/js/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="/resources/js/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/resources/js/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
</head>
<body class="fix-header fix-sidebar card-no-border">
<!-- ============================================================== -->
<!-- Preloader - style you can find in spinners.css -->
<!-- ============================================================== -->
<div class="preloader">
    <svg class="circular" viewBox="25 25 50 50">
        <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10"/>
    </svg>
</div>
<!-- ============================================================== -->
<!-- Main wrapper - style you can find in pages.scss -->
<!-- ============================================================== -->
<div id="main-wrapper">
    <!-- ============================================================== -->
    <!-- Topbar header - style you can find in pages.scss -->
    <!-- ============================================================== -->
    <header class="topbar">
        <nav class="navbar top-navbar navbar-toggleable-sm navbar-light">
            <!-- ============================================================== -->
            <!-- Logo -->
            <!-- ============================================================== -->
            <div class="navbar-header"></div>
            <!-- ============================================================== -->
            <!-- End Logo -->
            <!-- ============================================================== -->
            <div class="navbar-collapse">
                <!-- ============================================================== -->
                <!-- toggle and nav items -->
                <!-- ============================================================== -->
                <ul class="navbar-nav mr-auto mt-md-0">
                    <!-- This is  -->
                    <li class="nav-item"><a class="nav-link nav-toggler hidden-md-up text-muted waves-effect waves-dark"
                                            href="javascript:void(0)"><i class="mdi mdi-menu"></i></a></li>
                    <!-- ============================================================== -->
                    <!-- Search -->
                    <!-- ============================================================== -->
                    <li class="nav-item hidden-sm-down search-box"><a
                            class="nav-link hidden-sm-down text-muted waves-effect waves-dark"
                            href="javascript:void(0)"><i class="ti-search"></i></a>
                        <form class="app-search">
                            <input type="text" class="form-control" placeholder="Search & enter"> <a class="srh-btn"><i
                                class="ti-close"></i></a></form>
                    </li>
                </ul>
                <!-- ============================================================== -->
                <!-- User profile and search -->
                <!-- ============================================================== -->
                <ul class="navbar-nav my-lg-0">
                    <!-- ============================================================== -->
                    <!-- Profile -->
                    <!-- ============================================================== -->
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle text-muted waves-effect waves-dark" href=""
                           data-toggle="dropdown" aria-haspopup="true"
                           aria-expanded="false">欢迎您：<%=userName%>【角色】
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <!-- ============================================================== -->
    <!-- End Topbar header -->
    <!-- ============================================================== -->
    <!-- ============================================================== -->
    <!-- Left Sidebar - style you can find in sidebar.scss  -->
    <!-- ============================================================== -->
    <aside class="left-sidebar">
        <!-- Sidebar scroll-->
        <div class="scroll-sidebar">
            <!-- Sidebar navigation-->
            <nav class="sidebar-nav">
                <ul id="sidebarnav">
                    <%-- 管理员才可操作 --%>
                    <% if (userName.equals("admin")) {%>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=userManagement"
                           aria-expanded="false"><i
                            class="mdi mdi-gauge"></i><span class="hide-menu">用户管理</span></a>
                    </li>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=roleManagement"
                           aria-expanded="false"><i
                            class="mdi mdi-earth"></i><span class="hide-menu">角色管理</span></a>
                    </li>
                    <% } %>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=toDoList"
                           aria-expanded="false"><i
                            class="mdi mdi-account-check"></i><span class="hide-menu">待办任务管理</span></a>
                    </li>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=finishedList"
                           aria-expanded="false"><i
                            class="mdi mdi-table"></i><span class="hide-menu">已办任务管理</span></a>
                    </li>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=historyManagement"
                           aria-expanded="false"><i
                            class="mdi mdi-emoticon"></i><span class="hide-menu">历史任务管理</span></a>
                    </li>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=deployManagement"
                           aria-expanded="false"><i
                            class="mdi mdi-help-circle"></i><span class="hide-menu">流程部署管理</span></a>
                    </li>
                    <li><a class="waves-effect waves-dark" href="<%=basePath%>/views?pageName=defManagement"
                           aria-expanded="false"><i
                            class="mdi mdi-help-circle"></i><span class="hide-menu">流程定义管理</span></a>
                    </li>
                </ul>
                <div class="text-center m-t-30">
                    <a href="javascript:logout()"
                       class="btn waves-effect waves-light btn-warning hidden-md-down">退出登录</a>
                </div>
            </nav>
            <!-- End Sidebar navigation -->
        </div>
    </aside>
    <!-- ============================================================== -->
    <!-- End Left Sidebar - style you can find in sidebar.scss  -->
    <!-- ============================================================== -->
    <!-- ============================================================== -->
    <!-- Page wrapper  -->
    <div></div>
    <!-- End Page wrapper  -->
    <!-- ============================================================== -->
</div>
<!-- ============================================================== -->
<!-- End Wrapper -->
<!-- ============================================================== -->
<!-- ============================================================== -->
<!-- All Jquery -->
<!-- ============================================================== -->
<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<script src="/resources/assets/plugins/bootstrap/js/tether.min.js"></script>
<script src="/resources/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
<!-- slimscrollbar scrollbar JavaScript -->
<script src="/resources/js/jquery.slimscroll.js"></script>
<!--Wave Effects -->
<script src="/resources/js/waves.js"></script>
<!--Menu sidebar -->
<script src="/resources/js/sidebarmenu.js"></script>
<!--stickey kit -->
<script src="/resources/assets/plugins/sticky-kit-master/dist/sticky-kit.min.js"></script>
<!--Custom JavaScript -->
<script src="/resources/js/custom.min.js"></script>
<!-- ============================================================== -->
<!-- This page plugins -->
<!-- ============================================================== -->
<!-- chartist chart -->
<script src="/resources/assets/plugins/chartist-js/dist/chartist.min.js"></script>
<script src="/resources/assets/plugins/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.min.js"></script>
<!--c3 JavaScript -->
<script src="/resources/assets/plugins/d3/d3.min.js"></script>
<script src="/resources/assets/plugins/c3-master/c3.min.js"></script>
<!-- Chart JS -->
<script src="/resources/js/dashboard1.js"></script>

<script type="text/javascript">
    // 退出登陆
    function logout() {
        var msg = "确认退出？";
        if (confirm(msg) == true) {
            window.location.href = '<%=basePath%>/user/logout';
        }
    }

    // 导航栏
    function openTab(pageName) {
        window.location.href = '<%=basePath%>/views?pageName=' + pageName;
    }
</script>
</body>
</html>
