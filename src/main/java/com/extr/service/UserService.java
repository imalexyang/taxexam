package com.extr.service;

import java.util.List;
import java.util.Map;

import com.extr.domain.user.User;
import com.extr.util.Page;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午5:52:55
 */
public interface UserService {

	int addUser(User user);
	
	int addAdmin(User user);

	public List<User> getUserListByRoleId(int roleId,Page<User> page);
	
	public List<User> getUserListByRoleIdAndStatus(int roleId,int status,Page<User> page);
	
	public List<User> getUserListByIsall(String isall,Page<User> page);
	
	public List<User> getUserListByIsallAndIswin(String isall,String iswin);
	
	public List<User> getUserListByIsallAndIswinAndWinlevel(String isall,String iswin,String winlevel);	
	
	public void updateUser(User user,String oldPassword);
	
	public User getUserById(int user_id);
	
	public void disableUser(int user_id);
	
	public User getUserByName(String username);
	
	public void updateStatus(User user);
	
	public void updateIsall(User user);
	
	public void updateIswin(User user);
	
	public int countWinner(Map<String,Object> map);	
	
}
