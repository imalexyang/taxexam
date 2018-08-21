<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- <%@taglib uri="spring.tld" prefix="spring"%> --%>
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
		<title>第五届“税收和注册税务师知识竞赛”——试题预览</title>
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="resources/css/exam.css" rel="stylesheet" type="text/css">
		<link href="resources/css/style.css" rel="stylesheet">
		
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
						<li class="active">
							<a href="admin/question-list"><i class="fa fa-edit"></i>试题管理</a>
						</li>						
						<li>
							<a href="admin/user-list"><i class="fa fa-user"></i>会员管理</a>
						</li>	
						<li>
							<a href="admin/exam-paper-report/1"><i class="fa fa-file-text-o"></i>答题管理</a>
						</li>	
						<li>
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
								<a href="admin/question-list"> <i class="fa fa-list-ul"></i> 试题管理 </a>
							</li>
							<li class="active">
								<a> <i class="fa fa-file-text"></i> 试题预览 </a>

							</li>
						</ul>
					</div>
					<div class="col-xs-9">
						<div class="page-header">
							<h1 style="margin-left: -15px;"><i class="fa fa-file-text"></i> 试题预览 </h1>
						</div>
						<div class="page-content row">
							<div class="def-bk" id="bk-exampaper">

								<div class="expand-bk-content" id="bk-conent-exampaper">
									<ul id="exampaper-body" style="padding:0px;">
										${strHtml }						
										<div class="answer-desc-detail">
											<label class="label label-primary"><i class="fa fa-check-square-o"></i><span> 添加人</span></label>
											<div class="answer-desc-content">
												${question.creator }
											</div>
										</div>
									</ul>
									<div id="exampaper-footer" style="height: 187px;text-align:center;margin-top:40px;">
										<c:choose>
											<c:when test="${question.creator == sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
												<button class="btn btn-danger" id="delete-question-btn"><i class="fa fa-trash-o"></i> 删除该题</button>
												<button class="btn btn-info" onclick="javascript:window.close();"><i class="fa fa-times"></i> 关闭页面</button>
											</c:when>
											<c:otherwise>
												<button class="btn btn-danger" id="delete-question-btn" disabled="disabled" title="您只能删除你自己添加的题"><i class="fa fa-trash-o"></i> 删除该题</button>
												<button class="btn btn-info" onclick="javascript:window.close();"><i class="fa fa-times"></i> 关闭页面</button>
												<p>您只能删除你自己添加的题</p>
											</c:otherwise>
										</c:choose>
									</div>
								</div>
								

							</div>
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
		<script type="text/javascript" src="resources/js/all.js"></script>
		<script type="text/javascript" src="resources/js/jquery-ui-1.9.2.custom.min.js"></script>
		
		<script type="text/javascript" src="resources/js/uploadify/jquery.uploadify3.1Fixed.js"></script>
		<script type="text/javascript" src="resources/js/question-upload-img.js"></script>
		<script type="text/javascript" src="resources/js/field-2-point.js"></script>
		<script type="text/javascript" src="resources/js/question-add.js"></script>
		
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script>
								$(function(){
											$("#delete-question-btn").click(function(){
												var result = confirm("确定删除吗？删除后将不可恢复");
												if(result == true){
													jQuery.ajax({
														headers : {
															'Accept' : 'application/json',
															'Content-Type' : 'application/json'
														},
		  												type : "GET",
														url : 'admin/delete-question/' + $(".question-id").text(),
														success : function(message,tst,jqXHR) {
															if(!util.checkSessionOut(jqXHR))return false;
															if (message.result == "success") {
																util.success("删除成功！", function(){
																	window.opener.location.reload(false);
																	window.close();
																});
															} else {
																util.error("操作失败请稍后尝试");
															}
														},
														error : function(jqXHR, textStatus) {
															util.error("操作失败请稍后尝试");
														}
													});
												}
												
											});
											
											
										
										
									});
		</script>
	</body>
</html>