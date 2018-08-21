$(function() {

});

function refreshCount(){
	$.ajax({
		  type: 'post',
		  url: "admin/exam-paper-refreshAllRight",
		  data: "",
		  dataType: "json",
		  success: function(data){
			  if(data.status=="success"){
				  alert("重新统计成功");
				  location.reload();
			  }
	      }
	});
}