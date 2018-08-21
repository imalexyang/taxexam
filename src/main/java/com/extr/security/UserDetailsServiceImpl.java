package com.extr.security;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.extr.controller.ExamController;
import com.extr.domain.user.Role;
import com.extr.domain.user.User;
import com.extr.persistence.UserMapper;

@Component("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserInfo userInfo;
	@Autowired
	public UserMapper userMapper;
	

	public UserMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		
		User user = userMapper.getUserByName(username);
		if(user == null)
			throw new UsernameNotFoundException("user not found!");
		List<Role> roleList = userMapper.getRoleListByUserId(user.getId(),null);
		/*List<UserGroup> userGroupList = userMapper.getUserGroupListByUserId(user.getId());
		List<Job> jobList = userMapper.getJobListByUserId(user.getId(), null);*/
		user.setRoleListStack(roleList);
		String roles = "";
		String rolesName = "";
		for(int i=0;i<roleList.size();i++){
			roles += roleList.get(i).getAuthority() + ",";
			rolesName += roleList.get(i).getName() + ",";
		}
		roles = roles.substring(0, roles.length() - 1);
		rolesName = rolesName.substring(0, rolesName.length() - 1);
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
		userInfo = new UserInfo(username,user.getPassword(),user.getEnabled().equals("1"),true,true,true,authorities);
		userInfo.setRoleList(roleList);
		userInfo.setUserid(user.getId());
		userInfo.setTrueName(user.getTruename());
		userInfo.setRolesName(rolesName);
		userInfo.setEnabled(user.getEnabled());
		userInfo.setFieldId(user.getFieldId());
		userInfo.setFieldName(user.getFieldName());
		userInfo.setEmail(user.getEmail());
		userInfo.setLastLoginTime(user.getLastLoginTime());
		userInfo.setLoginTime(user.getLoginTime());
		userInfo.setPhone(user.getPhone());
		userInfo.setIdcard(user.getIdcard());
		return userInfo;
	}
	
	
	
}
