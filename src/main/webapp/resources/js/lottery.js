$(function() {	
	
});


function winlevelValue(){
	resultShow(0);
}

var status=0;
var num=0
function lotteryStart(parm){
	var winlevel=$("#winlevel option:selected").html();	
	num=parm
	
	
	var r=confirm("确定开始摇奖【"+winlevel+"  "+num+"人】吗？摇奖后，结果不可更改！")
	if (r==true)
    {
		var type=$("#winlevel").val();
		
		$("#demo").css("height",30*num);
		$("#winlevel").attr("disabled",true);
		$(".num").attr("disabled",true);
		$.ajax({
			  type: 'post',
			  url: "admin/lotteryStart",
			  data: "type="+type+"&num="+num,
			  dataType: "json",
			  success: function(data){
				  if(data.status=="success"){
					  var userhtml="";
					  if(data.type=="user"){
						  $.each(data.userlist, function(index,item){
							  userhtml=userhtml+"<div class='biaoge'>"+item.idcard+"</div>";
						   });
					  }else if(data.type=="org"){
						  $.each(data.orglist, function(index,item){
							  userhtml=userhtml+"<div class='biaoge'>"+item.name+"</div>";
						   });
					  }
					  $("#demo1").html(userhtml);
					  status=1;
					  afterStatus();
				  }
		      }
		});
    }	
}

function lotteryEnd(){
	if(status==0){
		alert("您还没点击抽奖人数");
	}else{
		var type=$("#winlevel").val();
		//var num=$("input[name='num']:checked").val();
		$.ajax({
			  type: 'post',
			  url: "admin/lotteryEnd",
			  data:"type="+type+"&num="+num,
			  dataType: "json",
			  success: function(data){
				  if(data.status=="success"){
					  var winnerhtml="";
					  if(data.type=="user"){
						  $.each(data.winerList, function(index,item){
							  	winnerhtml=winnerhtml+"<div class='biaoge'>"+item.idcard+"</div>";
						   });
					  }else if(data.type=="org"){
						  $.each(data.winerList, function(index,item){
							  	winnerhtml=winnerhtml+"<div class='biaoge'>"+item.name+"</div>";
						   });
					  }
					  $("#demo3").html(winnerhtml);
					  resultShow(1)
				  }
		      }
		});
		 status=0;
		 $("#winlevel").attr("disabled",false);
		 $(".num").attr("disabled",false);
	}	
}

function resultShow(type){
	if(type==1){
		$("#demo").hide();
		$("#demo3").show();
	}else if(type==0){
		$("#demo").show();
		$("#demo3").hide();		
	}
}

function afterStatus(){
	var speed=5;
	demo2.innerHTML=demo1.innerHTML;
	function Marquee(){		
	    if(demo2.offsetTop-demo.scrollTop<=0){
	        demo.scrollTop-=demo1.offsetHeight;
	    }
	    else{	    	
	    	demo.scrollTop=demo.scrollTop+1;	
	    }
	}
	var MyMar=setInterval(Marquee,speed);
	demo.onmouseover=function() {clearInterval(MyMar)}
	demo.onmouseout=function() {MyMar=setInterval(Marquee,speed)}
}

