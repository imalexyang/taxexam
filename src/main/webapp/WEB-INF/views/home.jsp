<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
		Remove this if you use the .htaccess -->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8"><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title>第五届“税收和注册税务师知识竞赛”——首页</title>
		<meta name="viewport"
		content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="keywords" content="">
		<link rel="shortcut icon" href="<%=basePath%>resources/images/favicon.ico" />
		<link href="resources/bootstrap/css/bootstrap-huan.css"
		rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.min.css"
		rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		
		<style>
			.question-number{
				color: #5cb85c;
				font-weight: bolder;
				display: inline-block;
				width: 30px;
				text-align: center;
			}
			
			.question-number-2{
				color: #5bc0de;
				font-weight: bolder;
				display: inline-block;
				width: 30px;
				text-align: center;
			}
			.question-number-3{
				color: #d9534f;
				font-weight: bolder;
				display: inline-block;
				width: 30px;
				text-align: center;
			}
			
			a.join-practice-btn{
				margin:0;
				margin-left:20px;
			}
			
			.content ul.question-list-knowledge{
				padding:8px 20px;
			}
			
			.knowledge-title{
				background-color:#EEE;
				padding:2px 10px;
				margin-bottom:20px;
				cursor:pointer;
				border:1px solid #FFF;
				border-radius:4px;
			}
			
			.knowledge-title-name{
				margin-left:8px;
			}
			
			.point-name{
				width:200px;
				display:inline-block;
			}
			
			/*下面两个样式是用于遮罩层  */
			#bg{ 
			  display: block;
			  position: absolute;
			  top: 0%;
		      left: 0%;
	          width: 100%;
	          height: 100%;
              background-color: black;
              z-index:1001;
              -moz-opacity: 0.7;
              opacity:.70; 
              filter: alpha(opacity=70);
			}
        	#show{
        	  	display: block;
        	    position: absolute;
        	    top: 20%;
	  	        left: 20%;  
	  	        width: 60%;  
	  	        height: 60%;  
	  	        padding: 8px;  
	  	        border: 8px solid #E8E9F7;  
	  	        background-color: white;  
	  	        z-index:1002;  
	  	        overflow: auto;
        	 }
        	 p{text-indent:2em} 
		</style>

	</head>

	<body>
		<header>
			<jsp:include  page="./commons/head.jsp"/>
		</header>
		<!-- Navigation bar starts -->

		<div class="navbar bs-docs-nav" role="banner">
			<div class="container">
				<nav class="collapse navbar-collapse bs-navbar-collapse" role="navigation">
					<ul class="nav navbar-nav">
						<li class="active">
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

		<!-- Slider starts -->
		<div class="content" style="margin-bottom: 100px;">
			<div class="container" style="height:330px;">
				<div>									
					<div class="row">
						<div class="select-test col-xs-6">
							<div style="height: 100px;">
								<div class="select-test-icon">
									<i class="fa fa-file-text"></i>
								</div>
								<div class="select-test-content">
									<h3 class="title">第五届“税收和注册税务师知识竞赛”</h3>									
									<a class="btn btn-primary" data-toggle="modal" data-target=".practice-exampaper-modal"><i class="fa fa-arrow-right"></i>开始竞赛</a>
									<br/>
									<br/>
									<div style="color: red;">${msg}</div>
									<div class="modal fade practice-exampaper-modal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
									  <div class="modal-dialog">
									    <div class="modal-content">
									    	<div class="modal-header">
										        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										        <h6 class="modal-title" id="myModalLabel">选择试卷，参加考试</h6>
										     </div>
										     <div class="modal-body">
										     	<ul>
										     		<c:forEach items="${practicepaper}" var="item">
										     			<li>
										     				<a href="student/examing/${item.id}">${item.name}</a>
										     			</li>
										     		</c:forEach>
										     	</ul>
										     </div>
										     <div class="modal-footer">
        										<button type="button" class="btn btn-default" data-dismiss="modal">关闭窗口</button>
      										 </div>									    	
									    </div>
									  </div>
									</div>
								</div>
								<!--//content-->
							</div>
						</div>
					</div>

				</div>

			</div>

		</div>
		
		<!-- 开始 -->	
		<div id="bg"></div>
		<div id="show">
			<div class="content">
				<h3 style="text-align: center;">国家税务总局办公厅  《注册税务师》杂志社</h3>
				<h3 style="text-align: center;">举办第五届“税收和注册税务师知识竞赛”活动启事</h3>
				<br/>
			     <p>2015年是中国注册税务师行业发展的关键年，按照国家税务总局党组书记、局长王军在全国税务工作会议上提出的全面加强税收宣传的要求，为了配合今年
			   		  深化税制改革和新税收政策的出台，依据国家税务总局税收宣传月“新常态、新税风”的部署，为进一步推进依法治税，提高全民税收法律遵从度，扩大注册税务师行业的社会影响，
			     	提高注册税务师从业人员的执业水平和服务质量，国家税务总局办公厅和《注册税务师》杂志社共同举办第五届“税收和注册税务师知识竞赛”活动。<p/>
			      <br/>
			     <p><b> 一、参赛范围</b><p/>
				 <p>参加竞赛人员为税务工作者、注册税务师、事务所从业人员、纳税人以及社会各界人员。</p>
 
				 <br/>
			     <p><b>二、竞赛形式</b><p/>					
				<p>(一)试题共60道，其中税收知识题50道，注册税务师知识题10道，均采用单项选择题。<p/>
				<p>(二) 4月1日在国家税务总局网站和《注册税务师》杂志社网站上发布知识竞赛活动启事，并在《注册税务师》杂志社网站上提供在线答题，国家税务总局网站上
				提供网上答题链接。<p/>
				<p>参赛人员答题后，请于6月30日前将答卷寄回《注册税务师》杂志社。地址：北京市海淀区阜成路73号裕惠大厦B座503室。邮编：100142。<p/>
				<p>（三）个人答卷按得分评奖。如全部正确答案多于所设奖项人数，则采取电脑抽奖方式决定。单位统一组织答卷，并统一邮寄答卷的，根据所组织答卷的总人数
				和答题正确率评组织奖。<p/>
				<p>（四）评选结果在《注册税务师》杂志、国家税务总局网站和《注册税务师》杂志社网站上公布，奖品或奖金由《注册税务师》杂志社邮寄。<p/>
				
				<br/>
			    <p><b>三、奖励办法</b><p/>				
				<p>竞赛分设个人奖和组织奖。个人奖52名，其中一等奖2名，奖品为价值为5000元的数码产品；二等奖10名，奖品为价值2000元的数码产品；三等奖20名，奖品
				为价值1000元的数码产品；优胜奖20名，奖品为价值500元的数码产品。组织奖10名，其中一等奖1名，奖金5000元；二等奖2名，奖金各3000元；三等奖3名，奖
				金各2000元，优胜奖4名，奖金各1000元。获奖者个人所得税由《注册税务师》杂志社代缴。<p/>
				
				<br/>
			    <p><b>四、注意事项</b><p/>	
			    <p>（一）本次竞赛试题均以2015年4月1日前发布的文件为依据作答<p/>			
				<p>（二）答题卡复印有效。<p/>
				<p>（三）纸质答题和在线答题均须注明身份证号等个人真实信息。<p/>
				<p>（四）截止时间以邮局邮戳和在线答题关闭为准。<p/>
				<p>（五）竞赛题的第二部分，可参考《注册税务师》杂志。<p/>
				<p>（六）为示公正，主办单位工作人员不参加本次竞赛。<p/>				
				
				<div style="float: right;margin-right: 60px;display: inline;clear: both;">国家税务总局办公厅</div>
		        <div style="float: right;margin-right: 60px;display: inline;clear: both;">《注册税务师》杂志社</div>
		        <div style="float: right;margin-right: 60px;display: inline;clear: both;"> </div>
				<div style="float: right;margin-right: 50px;display: inline;clear: both;">二〇一五年四月一日</div>				
			</div>
			<input id="btnclose" style="float: right;margin-right: 48%;display: inline;clear: both;" type="button" value="已阅读" onclick="hidediv();"/>
		</div>
		<!-- 结束 -->
		
		<footer>
			<jsp:include  page="./commons/foot.jsp"/>
		</footer>

		<!-- Slider Ends -->

		<!-- Javascript files -->
		<!-- jQuery -->
		<script type="text/javascript"
		src="resources/js/jquery/jquery-1.9.0.min.js"></script>
		<!-- Bootstrap JS -->
		<script type="text/javascript"
		src="resources/bootstrap/js/bootstrap.min.js"></script>
		
		<c:choose>
			<c:when test="${not empty sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username}">
				<script type="text/javascript">
					document.getElementById("bg").style.display ='none';
			    	document.getElementById("show").style.display ='none';
			    </script>
			</c:when>
		</c:choose>		
		<script>
		
		function hidediv() {
		    document.getElementById("bg").style.display ='none';
		    document.getElementById("show").style.display ='none';
		}		
		
		$(function(){
			bindQuestionKnowledage();
			var result = checkBrowser();
			if (!result){
				alert("请至少更新浏览器版本至IE8或以上版本");
			}
		});
		
		function checkBrowser() {
			var browser = navigator.appName;
			var b_version = navigator.appVersion;
			var version = b_version.split(";");
			var trim_Version = version[1].replace(/[ ]/g, "");
			if (browser == "Microsoft Internet Explorer" && trim_Version == "MSIE7.0") {
				return false;
			} else if (browser == "Microsoft Internet Explorer"
					&& trim_Version == "MSIE6.0") {
				return false;
			} else
				return true;
		}
		
		function bindQuestionKnowledage(){
			$(".knowledge-title").click(function(){
				var ul = $(this).parent().find(".question-list-knowledge");
				
				if(ul.is(":visible")){
					$(this).find(".fa-chevron-down").hide();
					$(this).find(".fa-chevron-up").show();
					
					$(".question-list-knowledge").slideUp();
					
				}else{
					$(".fa-chevron-down").hide();
					$(".fa-chevron-up").show();
					
					$(this).find(".fa-chevron-up").hide();
					$(this).find(".fa-chevron-down").show();
					
					$(".question-list-knowledge").slideUp();
					ul.slideDown();
					
				}
				
				
			});
		}
		</script>
	</body>
</html>
