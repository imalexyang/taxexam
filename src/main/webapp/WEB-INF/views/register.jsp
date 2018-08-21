<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
+ request.getServerName() + ":" + request.getServerPort()
+ path + "/";
%>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<!--
		<link rel="stylesheet" type="text/css" href="styles.css">
		-->
		<title>第五届“税收和注册税务师知识竞赛”——用户注册</title>
		<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css"
		rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		<!-- Javascript files -->
		<style type="text/css">
			.form-group {
				margin-bottom: 5px;
				height: 59px;
			}

		</style>
	</head>

	<body>
		<header>
			<jsp:include  page="./commons/head.jsp"/>
		</header>
		<!-- Navigation bar starts -->

		<div class="navbar bs-docs-nav" role="banner">
			<div class="container">
				<nav class="collapse navbar-collapse bs-navbar-collapse"
				role="navigation">
					<ul class="nav navbar-nav">
						<li>
							<a href="home"><i class="fa fa-home"></i>主页</a>
						</li>						
						<li>
							<a href="student/setting"><i class="fa fa-cogs"></i>个人设置</a>
						</li>
					</ul>
				</nav>
			</div>
		</div>

		<!-- Navigation bar ends -->

		<div class="content" style="margin-bottom: 100px;">

			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<div class="lrform">
							<h5>注册账号</h5>
							<span class="form-message"></span>
							<div class="form">
								<!-- Register form (not working)-->
								<form class="form-horizontal" id="form-create-account"
								action="user-reg">
									<!-- Name -->
									<div class="form-group form-username">
										<label class="control-label col-md-3" for="name">账号</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="name">
											<span class="form-message"></span>
										</div>

									</div>									
									<!-- password -->
									<div class="form-group form-password">
										<label class="control-label col-md-3" for="password">密码</label>
										<div class="col-md-9">
											<input type="password" class="form-control" id="password">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- password-confirm -->
									<div class="form-group form-password-confirm">
										<label class="control-label col-md-3" for="password-confirm">确认密码</label>
										<div class="col-md-9">
											<input type="password" class="form-control" id="password-confirm">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- truename -->
									<div class="form-group form-truename">
										<label class="control-label col-md-3" for="truename">真实姓名</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="truename">
											<span class="form-message"></span>
										</div>
									</div>									
									<!-- idcard -->
									<div class="form-group form-idcard">
										<label class="control-label col-md-3" for="idcard">身份证号</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="idcard">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- company -->
									<div class="form-group form-company">
										<label class="control-label col-md-3" for="company">工作单位</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="company">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- address -->
									<div class="form-group form-address">
										<label class="control-label col-md-3" for="address">通讯地址</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="address">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- zipcode -->
									<div class="form-group form-zipcode">
										<label class="control-label col-md-3" for="zipcode">邮政编码</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="zipcode">
											<span class="form-message"></span>
										</div>
									</div>
									<!-- tel -->
									<div class="form-group form-phone">
										<label class="control-label col-md-3" for="phone">手机/固话</label>
										<div class="col-md-9">
											<input type="text" class="form-control" id="phone">
											<span class="form-message"></span>
										</div>
									</div>
									
									<!-- province -->
									<div class="form-group form-province">
										<label class="control-label col-md-3" for="province">省份</label>
										<div class="col-md-9">
											<select class="form-control" id="province-input">
												<option value="-1">--请选择--</option>												
												<option value="1">北京</option>
												<option value="2">上海</option>
												<option value="3">天津</option>
												<option value="4">重庆</option>
												<option value="5">河北</option>
												<option value="6">山西</option>
												<option value="7">河南</option>
												<option value="8">辽宁</option>
												<option value="9">吉林</option>
												<option value="10">黑龙江</option>
												<option value="11">内蒙古</option>
												<option value="12">江苏</option>
												<option value="13">山东</option>
												<option value="14">安徽</option>
												<option value="15">浙江</option>
												<option value="16">福建</option>
												<option value="17">湖北</option>
												<option value="18">湖南</option>
												<option value="19">广东</option>
												<option value="20">广西</option>
												<option value="21">江西</option>
												<option value="22">四川</option>
												<option value="23">海南</option>
												<option value="24">贵州</option>
												<option value="25">云南</option>
												<option value="26">西藏</option>
												<option value="27">陕西</option>
												<option value="28">甘肃</option>
												<option value="29">青海</option>
												<option value="30">宁夏</option>
												<option value="31">新疆</option>
												<option value="32">台湾</option>
												<option value="33">香港</option>
												<option value="34">澳门</option>										
											</select>
											<span class="form-message"></span>
										</div>
									</div>	
									<!-- type -->							
									<div class="form-group form-type">
										<label class="control-label col-md-3">用户类型</label>
										<div class="col-md-9">
											<select class="form-control" id="type-input">
												<option value="-1">--请选择--</option>												
												<option value="1">税务师事务所</option>
												<option value="2">税务局</option>
												<option value="3">院校学生</option>
												<option value="4">其他</option>
											</select>
											<span class="form-message"></span>
										</div>
									</div>
									<!-- Buttons -->
									<div class="form-group">
										<!-- Buttons -->
										<div class="col-md-9 col-md-offset-3">
											<button type="submit" class="btn" id="btn-reg">
												注册账号
											</button>
											<button type="reset" class="btn">
												重置
											</button>
										</div>
									</div>
								</form>
								已有账号? <a href="user-login-page">直接登录</a>
							</div>
						</div>

					</div>
				</div>

			</div>

		</div>
		<footer>
			<jsp:include  page="./commons/foot.jsp"/>
		</footer>

		<!-- Slider Ends -->
		<!-- jQuery -->
		<script type="text/javascript" src="resources/js/jquery/jquery-1.9.0.min.js"></script>
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/js/register.js"></script>

	</body>
</html>
