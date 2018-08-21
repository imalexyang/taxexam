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
			.examing-point{
				display:block;
				font-size:10px;
				margin-top:5px;
			}
			.question-name-td{
				width:300px;
			}
			.change-property{
				cursor:pointer;
			}
			.add-tag-btn{
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
							<li class="active">
								<a href="admin/question-list"> <i class="fa fa-list-ul"></i> 试题管理 </a>
							</li>
							<li>
								<a href="admin/question-add"><i class="fa fa-list-ul"></i> 添加试题</a>
							</li>
							<li>
								<a href="admin/exampaper-list"> <i class="fa fa-list-ul"></i> 试卷管理 </a>
							</li>							
						</ul>
					</div>
					<div class="col-xs-9">
						<div class="page-header">
							<h1><i class="fa fa-list-ul"></i> 试题管理 </h1>
						</div>
						<div class="page-content row">

							<div id="question-filter">

								<dl id="question-filter-field">
									<dt>
										题库：
									</dt>
									<dd>
										<c:choose>
											<c:when test="${questionFilter.fieldId == 0 }">
												<span data-id="0" class="label label-info">全部</span>
											</c:when>
											<c:otherwise>
												<span data-id="0">全部</span>
											</c:otherwise>
										</c:choose>
										<c:forEach items="${fieldList}" var="field">
											<c:choose>
												<c:when test="${questionFilter.fieldId == field.fieldId }">
													<span class="label label-info" data-id="${field.fieldId}">${field.fieldName}</span>
												</c:when>
												<c:otherwise>
													<span data-id="${field.fieldId}">${field.fieldName}</span>
												</c:otherwise>
											</c:choose>	
										</c:forEach>
									</dd>
								</dl>
								<dl id="question-filter-knowledge">
									<dt>
										知识分类：
									</dt>
									<dd>
										<c:choose>
											<c:when test="${questionFilter.knowledge == 0 }">
												<span data-id="0" class="label label-info">全部</span>
											</c:when>
											<c:otherwise>
												<span data-id="0">全部</span>
											</c:otherwise>
										</c:choose>
										<c:forEach items="${knowledgeList}" var="knowledge">
											<c:choose>
												<c:when test="${questionFilter.knowledge == knowledge.pointId }">
													<span data-id="${knowledge.pointId}" class="label label-info">${knowledge.pointName}</span>
												</c:when>
												<c:otherwise>
													<span data-id="${knowledge.pointId}">${knowledge.pointName}</span>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</dd>
								</dl>

								<dl id="question-filter-qt">
									<dt>
										试题类型：
									</dt>
									<dd>
										<c:choose>
											<c:when test="${questionFilter.questionType == 0 }">
												<span data-id="0" class="label label-info">全部</span>
											</c:when>
											<c:otherwise>
												<span data-id="0">全部</span>
											</c:otherwise>
										</c:choose>
										<c:forEach items="${questionTypeList}" var="questionType">
											<c:choose>
												<c:when test="${questionFilter.questionType == questionType.id }">
													<span data-id="${questionType.id}" class="label label-info">${questionType.name}</span>
												</c:when>
												<c:otherwise>
													<span data-id="${questionType.id}">${questionType.name}</span>
												</c:otherwise>
											</c:choose>
											
											
										</c:forEach>

									</dd>
								</dl>
								<div class="page-link-content">
									<ul class="pagination pagination-sm">${pageStr}</ul>
								</div>
							</div>
							<div id="question-list">
								<input id="field-id-hidden" value="${fieldId }" type="hidden">
								<input id="knowledge-hidden" value="${knowledge }" type="hidden">
								<input id="question-type-hidden" value="${questionType }" type="hidden">
								<input id="search-param-hidden" value="${searchParam }" type="hidden">
								<table class="table-striped table">
									<thead>
										<tr>
											<td></td><td>ID</td><td class="question-name-td" style="width:240px">试题名称</td><td style="width:60px">试题类型</td><td>专业</td><td>知识类</td>
										</tr>
									</thead>
									<tbody>
										
										<c:forEach items="${questionList }" var="items">
											<tr>
												<td>
												<input type="checkbox" value="${items.id }">
												</td><td>${items.id }</td>
												<td>
													<a href="admin/question-preview/${items.id }" target="_blank" title="预览">${items.name }</a>
													<!-- 此处改成标签 -->
													<span class="examing-point">${items.examingPoint} </span>
												</td>
												
												
												<td>${items.questionTypeName }</td><td>${items.fieldName }</td><td>${items.pointName }</td>
												
										</tr>
										</c:forEach>
										

									</tbody><tfoot></tfoot>
								</table>
								<div class="modal fade" id="change-property-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
													  <div class="modal-dialog">
													    <div class="modal-content">
													    	<div class="modal-header">
														        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														        <h6 class="modal-title" id="myModalLabel">修改分类</h6>
														     </div>
														     <div class="modal-body">
														     	<form id="question-edit-form">
														     		<span id="add-update-questionid" style="display:none;"></span>
															     	
														     		<div class="form-line exampaper-type" id="aq-course1">
																		<span class="form-label"><span class="warning-label">*</span>专业：</span>
																		<select id="field-select" class="df-input-narrow">
																			<c:forEach items="${fieldList}" var="field">
																				<option value="${field.fieldId}">${field.fieldName} </option>
																			</c:forEach>
																		</select><span class="form-message"></span>
																	</div>
														     		<div class="form-line exampaper-type" id="aq-course2">
																		<span class="form-label"><span class="warning-label">*</span>知识类：</span>
																		<select id="point-from-select" class="df-input-narrow">
																			<c:forEach items="${knowledgeList}" var="item">
																				<option value="${item.pointId}">${item.pointName} </option>
																			</c:forEach>
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
		<!-- Bootstrap JS -->
		<script type="text/javascript" src="resources/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/js/question-list.js"></script>
		<script type="text/javascript" src="resources/js/all.js"></script>
		<script type="text/javascript" src="resources/js/field-2-point.js"></script>
		
		<script>
			$(function(){
				$(".change-property").click(function(){
					$("#change-property-modal").modal({backdrop:true,keyboard:true});
					var paper_id =  $(this).parent().parent().find(":checkbox").val();
					$("#add-update-questionid").text(paper_id);
					$.ajax({
						headers : {
							'Accept' : 'application/json',
							'Content-Type' : 'application/json'
						},
						type : "GET",
						url : "teacher/question-tag/" + paper_id,
						success : function(message, tst, jqXHR) {
							if (!util.checkSessionOut(jqXHR))
								return false;
							if (message.result == "success") {
								//将message.object里面的内容写到 div（class=q-label-list）里面
								var innerHtml = "";
								$.each(message.object,function(index,element){
									innerHtml += "<span class=\"label label-info q-label-item\" data-privatee=" 
										+ element.privatee + " data-creator=" + element.creator
										+" data-memo="+ element.memo
										+" data-id="+ element.tagId
										+ ">" + element.tagName + "  <i class=\"fa fa-times\"></i>	</span>";
								});
								$(".q-label-list").html(innerHtml);
							} else {
								util.error("操作失败请稍后尝试:" + message.result);
							}

						},
						error : function(jqXHR, textStatus) {
							util.error("操作失败请稍后尝试");
						}
					});					
				});
				
				$(".add-tag-btn").click(function(){
					var label_ids = $(".q-label-item");
					var flag = 0;
					label_ids.each(function(){
						if($(this).data("id") == $("#tag-from-select").val())
							flag = 1;
					});
					if(flag == 0){
						var selected = $("#tag-from-select").find("option:selected");
					
						$(".q-label-list").append("<span class=\"label label-info q-label-item\" data-privatee=" 
								+ selected.data("privatee")  + " data-creator=" + selected.data("creator") 
								+" data-memo="+ selected.data("memo") 
								+" data-id="+ $("#tag-from-select").val()
								+" data-createTime="+ selected.data("createTime") +">" 
								+ $("#tag-from-select :selected").text() + "  <i class=\"fa fa-times\"></i>	</span>");
					}
					else{
						util.error("不能重复添加");
					}
				});
				
				$("#update-exampaper-btn").click(function(){
					
					if($("#point-from-select").val()==null||$("#point-from-select").val()==""){
						util.error("请选择知识类");
					}
					$("#point-from-select").val();
					var data = new Array();
					
					$(".q-label-item").each(function(){
						var tag = new Object();
						tag.tagId = $(this).data("id");
						tag.questionId = $("#add-update-questionid").text();
						data.push(tag);
					});
					$.ajax({
						headers : {
							'Accept' : 'application/json',
							'Content-Type' : 'application/json'
						},
						type : "POST",
						url : "admin/question-update/" + $("#add-update-questionid").text() + "/" +  $("#point-from-select").val(),
						data : JSON.stringify(data),
						success : function(message, tst, jqXHR) {
							if (!util.checkSessionOut(jqXHR))
								return false;
							if (message.result == "success") {
								util.success("修改成功", function(){
									window.location.reload();
								});
							} else {
								util.error("操作失败请稍后尝试:" + message.result);
							}

						},
						error : function(jqXHR, textStatus) {
							util.error("操作失败请稍后尝试");
						}
					});
					
					return false;
				});
				
				$(".q-label-list").on("click",".fa",function(){
					$(this).parent().remove();
				});
				
				
			});
		
		
		
		</script>
	</body>
</html>
