$(function() {
	create_account.initial();
});



var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",
		34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",
		53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};

function cidInfo(sId){ 
var iSum=0 
var info="" 
if(!/^\d{17}(\d|x)$/i.test(sId))return false; 
sId=sId.replace(/x$/i,"a"); 
if(aCity[parseInt(sId.substr(0,2))]==null)return false//"Error:非法地区"; 
sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2)); 
var d=new Date(sBirthday.replace(/-/g,"/")) 
if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate()))return false//"Error:非法生日"; 
for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) 
if(iSum%11!=1)return false//"Error:非法证号"; 
return true//aCity[parseInt(sId.substr(0,2))]+","+sBirthday+","+(sId.substr(16,1)%2?"男":"女") 
} 

//去掉字符串头尾空格   
function trim(str) {   
    return str.replace(/(^\s*)|(\s*$)/g, "");   
}

var create_account = {

	initial : function initial() {
		this.bindSubmitForm();
	},

	bindSubmitForm : function bindSubmitForm() {
		var form = $("form#form-create-account");

		form.submit(function() {
					var result = create_account.verifyInput();
					if (result) {
						var data = new Object();
						data.username = trim($("#name").val());						
						data.password = trim($("#password").val());
						data.truename = trim($("#truename").val());
						data.idcard = trim($("#idcard").val());
						data.company = trim($("#company").val());
						data.address = trim($("#address").val());
						data.zipcode = trim($("#zipcode").val());					
						data.phone = trim($("#phone").val());
						data.province = $("#province-input").val();
						data.type = $("#type-input").val();
						jQuery
								.ajax({
									headers : {
										'Accept' : 'application/json',
										'Content-Type' : 'application/json'
									},
									type : "POST",
									url : form.attr("action"),
									data : JSON.stringify(data),
									success : function(message, tst, jqXHR) {
										if (message.result == "success") {
											document.location.href = document
													.getElementsByTagName('base')[0].href
													+ "regist-success/"
													+ data.username;
										} else {
											if (message.result == "duplicate-username") {
												$(
														".form-username .form-message")
														.text(
																message.messageInfo);
											} else if (message.result == "captch-error") {
												
											} else if (message.result == "duplicate-email") {
												$(
														".form-email .form-message")
														.text(
																message.messageInfo);
											} else {
												alert(message.result);
											}
										}
									}
								});
					}

					return false;
				});
	},

	verifyInput : function verifyInput() {
		$(".form-message").empty();
		var result = true;
		var check_u = this.checkUsername();		
		var check_p = this.checkPassword();
		var check_cp = this.checkConfirmPassword();
		var checkt_tr = this.checktTruename();
		var check_id = this.checkIdcard();
		var check_co = this.checkCompany();
		var check_ad = this.checkAddress();
		var check_zi = this.checkZipcode();
		var check_ph = this.checkPhone();
		var check_pr = this.checkProvince();
		var check_ty = this.checkType();
		
		result = check_u  && check_p && check_cp && checkt_tr && check_id && check_co && check_ad && check_zi && check_ph && check_pr && check_ty;
		return result;
	},

	checkUsername : function checkUsername() {
		var username = trim($(".form-username input").val());
		if (username == "") {
			$(".form-username .form-message").text("用户名不能为空");
			return false;
		} else if (username.length > 20 || username.length < 5) {
			$(".form-username .form-message").text("请保持在5-20个字符以内");
			return false;
		} else {
			var re=/[\+|\-|\\|\/||&|!|~|@|#|\$|%|\^|\*|\(|\)|=|\?|´|"|<|>|\.|,|:|;|\]|\[|\{|\}|\|]+/;
			if(re.test(username)){
				$(".form-username .form-message").text("只能是数字字母或者下划线的组合");
				return false;
			}else return true; 
			
			
		}
		return true;
	},

	checkPassword : function checkPassword() {
		var password = trim($(".form-password input").val());
		if (password == "") {
			$(".form-password .form-message").text("密码不能为空");
			return false;
		} else if (password.length < 6 || password.length > 20) {
			$(".form-password .form-message").text("密码请保持在6到20个字符以内");
			return false;
		} else {
			return true;
		}
		return true;
	},

	checkConfirmPassword : function checkConfirmPassword() {
		var password_confirm = trim($(".form-password-confirm input").val());
		var password = $(".form-password-confirm input").val();
		if (password_confirm == "") {
			$(".form-password-confirm .form-message").text("请再输入一次密码");
			return false;
		} else if (password_confirm.length > 20) {
			$(".form-password-confirm .form-message").text(
					"内容过长，请保持在20个字符以内");
			return false;
		} else if (password_confirm != password) {
			$(".form-password-confirm .form-message").text("2次密码输入不一致");
			return false;
		} else {
			return true;
		}
	},
	
	checktTruename : function checktTruename() {
		var truename = trim($(".form-truename input").val());
		if (truename == "") {
			$(".form-truename .form-message").text("真实姓名不能为空");
			return false;
		} else if (truename.length < 2 || truename.length > 10) {
			$(".form-truename .form-message").text("密码请保持在2到10个字符以内");
			return false;
		} else {
			return true;
		}
		return true;
	},
	
	checkIdcard : function checkIdcard() {
		var idcard = trim($(".form-idcard input").val());
		if (idcard == "") {
			$(".form-idcard .form-message").text("身份证号不能为空");
			return false;
		} else if (idcard.length != 15 && idcard.length != 18) {
			$(".form-idcard .form-message").text("请输入15或18位身份证号码");
			return false;
		} else {
			if(!cidInfo(idcard)){
				$(".form-idcard .form-message").text("请核对您的身份证号是否真实有效");
				return false;
			}else return true; 
			
			
		}
		return true;
	},
	
	checkCompany : function checkCompany() {
		var company = trim($(".form-company input").val());
		if (company == "") {
			$(".form-company .form-message").text("工作单位不能为空");
			return false;
		} else if (company.length < 6 || company.length > 40) {
			$(".form-company .form-message").text("工作单位请保持在6到40个字符以内");
			return false;
		} else {
			return true;
		}
		return true;
	},
	
	checkAddress : function checkAddress() {
		var address = $(".form-address input").val();
		if (address == "") {
			$(".form-address .form-message").text("通讯地址不能为空");
			return false;
		} else if (address.length < 10 || address.length > 100) {
			$(".form-address .form-message").text("通讯地址请保持在10到100个字符以内");
			return false;
		} else {
			return true;
		}
		return true;
	},
	
	checkZipcode : function checkZipcode() {
		var zipcode = $(".form-zipcode input").val();
		if (zipcode == "") {
			$(".form-zipcode .form-message").text("邮政编码不能为空");
			return false;
		} else if (zipcode.length !=6) {
			$(".form-zipcode .form-message").text("邮政编码为6位数字");
			return false;
		} else {	
			var reg = new RegExp("^[0-9]*$");  
			if(!reg.test(zipcode)){				
				$(".form-zipcode .form-message").text("邮政编码为6位数字");
				return false;
			}else return true; 
		}
		return true;
	},
	
	checkPhone : function checkPhone() {
		var phone = $(".form-phone input").val();
		if (phone == "") {
			$(".form-phone .form-message").text("手机/固话不能为空");
			return false;
		}else if (phone.length < 11 || phone.length > 20 ) {
			$(".form-phone .form-message").text("手机号码11位/固话带区号分机号11-20位");
			return false;
		} else {
			var re1=new RegExp("^[0-9]*$");			
			if(!re1.test(phone)){
				$(".form-phone .form-message").text("请输入正确的手机/固话格式，仅限数字，手机号码11位/固话带区号分机号11-20位");
				return false;
			}else return true; 
		}
		return true;
	},
	
	checkProvince : function checkProvince (){
		var province = $("#province-input").val();
		if(province == -1){
			$(".form-province .form-message").text("请选择省份");
			return false;
		}else{
			return true;
		}
		return false;
	},
	
	checkType : function checkType (){
		var type = $("#type-input").val();
		if(type == -1){
			$(".form-type .form-message").text("请选择用户类型");
			return false;
		}else{
			return true;
		}
		return false;
	}
};