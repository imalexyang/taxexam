<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>第五届“税收和注册税务师知识竞赛”——开奖管理</title>
		
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		
		<link href="resources/css/exam.css" rel="stylesheet">
		<link href="resources/chart/morris.css" rel="stylesheet">
	</head>
	<body>
		<header>
			<jsp:include  page="../commons/head.jsp"/>
		</header>
		<!-- Navigation bar starts -->

		<div class="navbar bs-docs-nav" role="banner">
			<div class="container">
				<nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
					<ul class="nav navbar-nav">
						<li >
							<a href="admin/question-list"><i class="fa fa-edit"></i>试题管理</a>
						</li>												
						<li>
							<a href="admin/user-list"><i class="fa fa-user"></i>会员管理</a>
						</li>
						<li>
							<a href="admin/exam-paper-report/1"><i class="fa fa-file-text-o"></i>答题管理</a>
						</li>	
						<li class="active">
							<a href="admin/lottery"><i class="fa fa-cloud"></i>抽奖管理</a>
						</li>
					</ul>
				</nav>
			</div>
		</div>

		<!-- Navigation bar ends -->

		<!-- Slider starts -->

		<div>
			<!-- Slider (Flex Slider) -->

			<div class="container" style="min-height:500px;">

				<div class="row">
					<div class="col-xs-3">
						<ul class="nav default-sidenav">
							<li>							
								<a href="admin/lottery"> <i class="fa fa-bar-chart-o"></i>抽奖管理</a>
							</li>
							<li class="active">
								<a href="admin/win-userlist"> <i class="fa fa-bar-chart-o"></i>获奖个人名单 </a>
							</li>
							<li>
								<a href="admin/win-orglist"> <i class="fa fa-bar-chart-o"></i>获奖组织名单 </a>
							</li>							
						</ul>

					</div>
					<div class="col-xs-9">
						<div class="page-header">
							<h1><i class="fa fa-bar-chart-o"></i> 获奖名单 </h1>
						</div>				
						<div class="page-content row">
							<table class="table table-bordered" style="margin-top: 25px;">
								<thead>
									<tr>
									    <th>级别</th>
										<th>账号</th>
										<th>真实姓名</th>	
										<th>手机号</th>									
										<th>身份证号</th>
										<th>通讯地址</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${winerList}" var="user">
										<tr>
											<td>${user.winlevel}</td>
											<td>${user.username }</td>
											<td>${user.truename }</td>										
											<td>${user.phone}</td>
											<td>${user.idcard}</td>
											<td>${user.address}</td>											
										</tr>
									</c:forEach>									
								</tbody>
							</table>
							<%-- <div id="page-link-content">
								<ul class="pagination pagination-sm">${pageStr}</ul>
							</div> --%>
						</div>
					</div>
				</div>
			</div>
		</div>

		<footer>
			<jsp:include  page="../commons/foot.jsp"/>
		</footer>

		<!-- Slider Ends -->

		<!-- Javascript files -->
		<!-- jQuery -->
		<script type="text/javascript" src="resources/js/jquery/jquery-1.9.0.min.js"></script>
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/chart/raphael-min.js"></script>
		<script type="text/javascript" src="resources/chart/morris.js"></script>
		<script type="text/javascript" src="resources/js/exam-paper-isall.js"></script>
	</body>
</html>
