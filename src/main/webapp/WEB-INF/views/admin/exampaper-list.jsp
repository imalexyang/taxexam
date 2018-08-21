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
		<title>第五届“税收和注册税务师知识竞赛”——试卷管理</title>
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		
		<link href="resources/css/exam.css" rel="stylesheet">
		<link href="resources/chart/morris.css" rel="stylesheet">
		<style>
			.change-property, .publish-paper, .delete-paper, .offline-paper{
				cursor:pointer;
			}
		</style>
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
							<li>
								<a href="admin/question-add"><i class="fa fa-list-ul"></i> 添加试题</a>
							</li>
							<li class="active">
								<a href="admin/exampaper-list"> <i class="fa fa-list-ul"></i> 试卷管理 </a>
							</li>
						</ul>

					</div>
					<div class="col-xs-9">
						<div class="page-header">
							<h1><i class="fa fa-list-ul"></i> 试卷管理 </h1>
						</div>
						<div class="page-content row">

							<div id="question-filter">
								
								

								<div class="page-link-content">
									<ul class="pagination pagination-sm">${pageStr}</ul>
								</div>
							</div>
							<div id="question-list">
								<table class="table-striped table">
									<thead>
										<tr>
											<td></td><td>ID</td><td>试卷名称</td><td>类别</td><td>创建人</td><td>状态</td><td>操作</td>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${paper }" var="item">
											<tr>
												<td>
													<input type="checkbox" value="${item.id }">
												</td>
												<td>${item.id }</td>
												<td><a href="admin/exampaper-preview/${item.id }" target="_blank" title="预览" class="td-paper-name">${item.name }</a></td>
												
												<td>
													<c:if test="${item.paper_type == '1' }">
														<span class="td-paper-type" data-id="${item.paper_type}">
															随机组卷
														</span>
													</c:if>
													<c:if test="${item.paper_type == '2' }">
														<span class="td-paper-type" data-id="${item.paper_type}">
														模拟考试
														</span>
													</c:if>
													<c:if test="${item.paper_type == '3' }">
														<span class="td-paper-type" data-id="${item.paper_type}">
														专家试卷
														</span>
													</c:if>
												</td>
												<td>${item.creator }</td>
												<td>
													<c:choose>
														<c:when test="${item.status == 0 }">
															未上线
														</c:when>
														<c:when test="${item.status == 1 }">
															已上线
														</c:when>
														<c:when test="${item.status == 2 }">
															已下线
														</c:when>
													</c:choose>
												</td>
												<td>
													<c:choose>
														<c:when test="${item.status == 0 }">
															<a href="admin/exampaper-edit/${item.id}">修改内容</a>
															<a class="change-property">修改属性</a>
															<a class="publish-paper">上线</a>
															<a class="delete-paper">删除</a>
														</c:when>
														<c:when test="${item.status == 1 }">
															<a class="offline-paper">下线</a>
														</c:when>
													</c:choose>
												
												</td>
											</tr>
										</c:forEach>
										
									</tbody><tfoot></tfoot>
								</table>
								<div class="modal fade" id="change-property-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
													  <div class="modal-dialog">
													    <div class="modal-content">
													    	<div class="modal-header">
														        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														        <h6 class="modal-title" id="myModalLabel">修改试卷属性</h6>
														     </div>
														     <div class="modal-body">
														     	<form id="question-add-form">
														     		<span id="add-update-exampaperid" style="display:none;"></span>
															     	<div class="form-line add-update-exampapername">
																		<span class="form-label"><span class="warning-label">*</span>试卷名称：</span>
																		<input type="text" class="df-input-narrow">
																		<span class="form-message"></span>
																	</div>
																	<div class="form-line add-update-duration">
																		<span class="form-label"><span class="warning-label">*</span>时长（秒）：</span>
																		<input type="text" class="df-input-narrow">
																		<span class="form-message"></span>
																	</div>
														     		<div class="form-line exampaper-type" id="exampaper-type">
																		<span class="form-label"><span class="warning-label">*</span>分类：</span>
																		<select id="exampaper-type-select" class="df-input-narrow">
																			
																			<option value="1">随机组卷</option>
																			<option value="2">模拟考试</option>
																			<option value="3">专家试卷</option>
																		</select><span class="form-message"></span>
																	</div>
																</form>
														     </div>
														     <div class="modal-footer">
				        										<button type="button" class="btn btn-default" data-dismiss="modal">关闭窗口</button>
				        										<button id="update-exampaper-btn" type="button" class="btn btn-primary">确定修改</button>
				      										 </div>
													    </div>
													  </div>
								</div>
							</div>
							<div class="page-link-content">
									<ul class="pagination pagination-sm">${pageStr}</ul>
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
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/js/exampaper-list.js"></script>
	</body>
</html>