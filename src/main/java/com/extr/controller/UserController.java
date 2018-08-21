package com.extr.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.extr.controller.domain.Message;
import com.extr.domain.question.Field;
import com.extr.domain.user.Org;
import com.extr.domain.user.User;
import com.extr.security.UserInfo;
import com.extr.service.OrgService;
import com.extr.service.QuestionService;
import com.extr.service.UserService;
import com.extr.util.Page;
import com.extr.util.PagingUtil;
import com.extr.util.StandardPasswordEncoderForSha1;

@Controller
public class UserController {

	public static final String SUCCESS_MESSAGE = "success";
	public static final String ERROR_MESSAGE = "failed";

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private QuestionService questionService;
	
	/**
	 * 用户登录页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/user-login-page" }, method = RequestMethod.GET)
	public String loginPage(Model model, @RequestParam(value = "result", required = false, defaultValue = "") String result) {
		if("failed".equals(result)){
			model.addAttribute("result", "无效的用户名或者密码");
		}
		return "login";
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/user-login" }, method = RequestMethod.POST)
	public @ResponseBody
	Message userLogin(@RequestBody User user) {
		user.setCreate_date(new Date());
		Message message = new Message();
		try {
			userService.addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message.setResult("error");
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 用户登录成功页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/regist-success/{username}" }, method = RequestMethod.GET)
	public String registerSuccessPage(@PathVariable String username, Model model) {
		model.addAttribute("username", username);
		return "regist-success";
	}
	
	
	/**
	 * 用户信息查看
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/user-detail/{username}" }, method = RequestMethod.GET)
	public String userDetailPage(@PathVariable String username, Model model) {
		model.addAttribute("username", username);
		return "redirect:/student/usercenter";
	}

	/**
	 * 用户注册页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/user-register" }, method = RequestMethod.GET)
	public String registerPage(Model model) {
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		return "register";
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/user-reg" }, method = RequestMethod.POST)
	public @ResponseBody
	Message userRegister(@RequestBody User user) {
		user.setCreate_date(new Date());
		String password = user.getPassword() + "{" + user.getUsername() + "}";
		PasswordEncoder passwordEncoder = new StandardPasswordEncoderForSha1();
		String resultPassword = passwordEncoder.encode(password);
		user.setPassword(resultPassword);
		user.setEnabled("1");
		Message message = new Message();
		try {
			userService.addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			if(e.getMessage().equals("duplicate-username")){
				message.setResult(e.getMessage());
				message.setMessageInfo("用户名：" + user.getUsername() + "已经存在");
			}else
				message.setResult("错误！" + e.getClass().getName());
			e.printStackTrace();
		}
		return message;
	}
	
	
	/**
	 * 修改密码页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "student/change-password" }, method = RequestMethod.GET)
	public String changePasswordPage() {
		return "student/change-password";
	}
	
	@RequestMapping(value = { "change-pwd" }, method = RequestMethod.POST)
	public @ResponseBody Message changePassword(@RequestBody User user){
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		try{
			String password = user.getPassword() + "{" + userInfo.getUsername() + "}";
			PasswordEncoder passwordEncoder = new StandardPasswordEncoderForSha1();
			String resultPassword = passwordEncoder.encode(password);
			user.setPassword(resultPassword);
			user.setUsername(userInfo.getUsername());
			userService.updateUser(user, null);
			System.out.println(user.getFieldId());
		}catch(Exception e){
			e.printStackTrace();
			message.setResult(e.getClass().getName());
		}
		
		return message;
	}
	/**
	 * 修改密码页面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "student/setting" }, method = RequestMethod.GET)
	public String settingPage(Model model) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		User user = userService.getUserById(userInfo.getUserid());
		model.addAttribute("user", user);
		return "student/setting";
	}
	
	@RequestMapping(value = { "student/setting" }, method = RequestMethod.POST)
	public String setting(User user){
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		user.setId(userInfo.getUserid());
		try{
			userService.updateUser(user, null);
		}catch(Exception e){
			e.printStackTrace();			
		}
		return "redirect:/student/setting";
	}	
	
	/**
	 * 显示用户管理界面
	 * 
	 * @return
	 */
	@RequestMapping(value = { "admin/user-list" }, method = RequestMethod.GET)
	public String showUserListPage(Model model, HttpServletRequest request) {
		
		int index = 1;
		if(request.getParameter("page") != null)
			index = Integer.parseInt(request.getParameter("page"));
		Page<User> page = new Page<User>();
		page.setPageNo(index);
		page.setPageSize(20);
		
		List<User> userList = userService.getUserListByRoleId(3, page);
		String pageStr = PagingUtil.getPagelink(index, page.getTotalPage(), "", "admin/user-list");
		model.addAttribute("userList", userList);
		model.addAttribute("pageStr", pageStr);
		return "admin/user-list";
	}
	
	/**
	 * 添加用户界面
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/add-user", method = RequestMethod.GET)
	private String addUserPage(Model model, HttpServletRequest request) {
			
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		return "admin/add-user";
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = { "/admin/add-user" }, method = RequestMethod.POST)
	public @ResponseBody Message addUser(@RequestBody User user){
		user.setCreate_date(new Date());
		String password = user.getPassword() + "{" + user.getUsername() + "}";
		PasswordEncoder passwordEncoder = new StandardPasswordEncoderForSha1();
		String resultPassword = passwordEncoder.encode(password);
		user.setPassword(resultPassword);
		user.setEnabled("1");
		Message message = new Message();
		try {
			userService.addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			if(e.getMessage().equals("duplicate-username")){
				message.setResult(e.getMessage());
				message.setMessageInfo("用户名：" + user.getUsername() + "已经存在");
			}else
				message.setResult("错误！" + e.getClass().getName());
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * 禁用用户
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/disable-user/{userId}", method = RequestMethod.GET)
	public @ResponseBody Message disableUser(Model model, HttpServletRequest request, @PathVariable Integer userId) {
		
		Message message = new Message();
		try{
			User user = new User();
			user.setId(userId);
			user.setEnabled("0");
			userService.updateUser(user, null);
		}catch(Exception e){
			e.printStackTrace();
			message.setResult(e.getClass().getName());
		}
		
		return message;
	}
	
	/**
	 * 启用用户
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/enable-user/{userId}", method = RequestMethod.GET)
	public @ResponseBody Message enableUser(Model model, HttpServletRequest request, @PathVariable Integer userId) {
		
		Message message = new Message();
		try{
			User user = new User();
			user.setId(userId);
			user.setEnabled("1");
			userService.updateUser(user, null);
		}catch(Exception e){
			e.printStackTrace();
			message.setResult(e.getClass().getName());
		}
		
		return message;
	}
	
	//往下都是抽奖管理
	
	@RequestMapping(value="admin/lottery",method = RequestMethod.GET)
	public String lotteryPage(){
		
		return "admin/lottery";
	}
	
	@RequestMapping(value="admin/win-userlist",method = RequestMethod.GET)
	public String winUserList(Model model){
		List<User> winerList=userService.getUserListByIsallAndIswin("1", "1");		
		for(User user:winerList){
			String wl=user.getWinlevel();
			if(wl.equals("1")){
				user.setWinlevel("个人一等奖");
			}else if(wl.equals("2")){
				user.setWinlevel("个人二等奖");
			}else if(wl.equals("3")){
				user.setWinlevel("个人三等奖");
			}else if(wl.equals("4")){
				user.setWinlevel("个人优胜奖");
			}else{
				user.setWinlevel("未中奖");
			}
		}
		
		model.addAttribute("winerList", winerList);
		return "admin/win-userlist";
	}
	
	@RequestMapping(value="admin/win-orglist",method = RequestMethod.GET)
	public String winOrgList(Model model){
		List<Org> winerList=orgService.getOrgListByIsallAndIswin("1", "1");		
		for(Org org:winerList){
			String wl=org.getWinlevel();
			if(wl.equals("5")){
				org.setWinlevel("组织一等奖");
			}else if(wl.equals("6")){
				org.setWinlevel("组织二等奖");
			}else if(wl.equals("7")){
				org.setWinlevel("组织三等奖");
			}else if(wl.equals("8")){
				org.setWinlevel("组织优胜奖");
			}else{
				org.setWinlevel("未中奖");
			}
		}
		
		model.addAttribute("winerList", winerList);
		return "admin/win-orglist";
	}
	
	@RequestMapping(value="admin/lotteryStart",method = RequestMethod.POST)
	@ResponseBody
	public Object lotteryStart(HttpServletRequest request,Integer type,Integer num){
		Map<String,Object> map=new HashMap<String,Object>();
		if(type.equals(1)||type.equals(2)||type.equals(3)||type.equals(4)){		
			List<User> userlist=userService.getUserListByIsallAndIswin("1", "0");
			map.put("userlist", userlist);
			map.put("status", "success");
			map.put("type", "user");				
		}else if(type.equals(5)||type.equals(6)||type.equals(7)||type.equals(8)){			
			List<Org> orglist=orgService.getOrgListByIsallAndIswin("1", "0");
			map.put("orglist", orglist);
			map.put("status", "success");
			map.put("type", "org");			
		}		
		return map;
	}
	
	@RequestMapping(value="admin/lotteryEnd")
	@ResponseBody
	public Object lotteryEnd(HttpServletRequest request,Integer type,Integer num){
		//type; //获奖等级
		//num=3;  //获奖人数	
		Map<String,Object> map=new HashMap<String,Object>();
		if(type.equals(1)||type.equals(2)||type.equals(3)||type.equals(4)){
			List<User> listAll=userService.getUserListByIsallAndIswin("1", "0"); //获取可以参与摇奖，并且未中奖的用户
			List<User> listWill=new ArrayList<User>();
			List<User> winerList=new ArrayList<User>();		
			int winCount=0;
			for(User user:listAll){
				if(user.getChance().equals(type)){
					user.setIswin("1");
					user.setWinlevel(""+type);
					userService.updateIswin(user);
					winerList.add(user);
					winCount++;
				}else if(user.getChance().equals(0)){
					listWill.add(user);
				}
			}
			num=num-winCount;
			if(listWill.size()>=num&&num>0){
				for(int i=0;i<num;i++){					
					
					/*int winNum=(int)(Math.random()*listWill.size());
					User winer=listWill.get(winNum);					
					winer.setIswin("1");
					winer.setWinlevel(""+type);
					userService.updateIswin(winer);
					listWill.remove(winNum);*/
					
					//如果同一个省出现两名以上，同一个单位出现一名以上，则重新抽奖
					User winer=winner(listWill,type);
					winerList.add(winer);
				}			
			}
			map.put("winerList", winerList);
			map.put("type", "user");
		}else if(type.equals(5)||type.equals(6)||type.equals(7)||type.equals(8)){
			List<Org> listAll=orgService.getOrgListByIsallAndIswin("1", "0"); //获取可以参与摇奖，并且未中奖的用户
			List<Org> listWill=new ArrayList<Org>();
			List<Org> winerList=new ArrayList<Org>();		
			int winCount=0;
			for(Org org:listAll){
				if(org.getChance().equals(type)){
					org.setIswin("1");
					org.setWinlevel(""+type);
					orgService.updateIswin(org);
					winerList.add(org);
					winCount++;
				}else if(org.getChance().equals(0)){
					listWill.add(org);
				}
			}
			num=num-winCount;
			if(listWill.size()>=num&&num>0){
				for(int i=0;i<num;i++){			
					
					/*int winNum=(int)(Math.random()*listWill.size());
					Org winer=listWill.get(winNum);
					winer.setIswin("1");
					winer.setWinlevel(""+type);
					orgService.updateIswin(winer);
					listWill.remove(winNum);*/
					
					//如果同一个省出现两名以上，同一个单位出现一名以上，则重新抽奖
					Org winner=winOrg(listWill,type);
					winerList.add(winner);
				}			
			}
			map.put("winerList", winerList);
			map.put("type", "org");
		}
		map.put("status", "success");
		return map;
	}	
	
	public User winner(List<User> listWill,Integer type){
		int winNum=(int)(Math.random()*listWill.size());
		User winner=listWill.get(winNum);
		
		Map<String,Object> proParm=new HashMap<String,Object>();
		proParm.put("province", winner.getProvince());
		Map<String,Object> comParm=new HashMap<String,Object>();
		comParm.put("company", winner.getCompany());
		int proviceNum=userService.countWinner(proParm);
		int companyNum=userService.countWinner(comParm);
		if(proviceNum>=2||companyNum>=1){			
			winner=winner(listWill,type);			
		}else{
			winner.setIswin("1");
			winner.setWinlevel(""+type);
			userService.updateIswin(winner);
			listWill.remove(winNum);
		}		
		return winner;
	}
	
	public Org winOrg(List<Org> listWill,Integer type){
		int winNum=(int)(Math.random()*listWill.size());
		Org winner=listWill.get(winNum);
		
		Map<String,Object> proParm=new HashMap<String,Object>();
		proParm.put("province", winner.getProvince());
		Map<String,Object> nameParm=new HashMap<String,Object>();
		nameParm.put("name", winner.getName());
		
		int proNum=orgService.countWinOrg(proParm);
		int nameNum=orgService.countWinOrg(nameParm);
		if(proNum>=2||nameNum>=1){
			winner=winOrg(listWill,type);
		}else{
			winner.setIswin("1");
			winner.setWinlevel(""+type);
			orgService.updateIswin(winner);
			listWill.remove(winNum);
		}		
		return winner;
	}
	
}
