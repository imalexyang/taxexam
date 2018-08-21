package com.extr.persistence;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.extr.domain.user.Role;
import com.extr.domain.user.User;
import com.extr.util.Page;

public interface UserMapper {

	/**
	 * 添加user并返回该记录的主键
	 * 
	 * @param user
	 * @return
	 */
	public int insertUser(User user);

	/**
	 * 更新user基本信息(包括更新password,fullname)
	 * 
	 * @param user
	 */
	public void updateUser(@Param("user") User user, @Param("oldpassword") String oldpassword);

	/**
	 * 根据ID删除某个用户的记录
	 * 
	 * @param user_id
	 */
	public void deleteUser(int user_id);

	/**
	 * 根据ID查询用户基本信息
	 * 
	 * @param user_id
	 * @return
	 */
	public User getUserById(int user_id);

	/**
	 * 根据用户名称查询用户基本信息
	 * 
	 * @param user_name
	 * @return
	 */
	public User getUserByName(String username);

	/**
	 * 获取用户列表
	 * 
	 * @return
	 */
	public List<User> getAllUserList(@Param("page") Page<User> page);

	/**
	 * 插入角色
	 * 
	 * @param role
	 * @return
	 */
	public int insertRole(Role role);

	/**
	 * 更新角色
	 * 
	 * @param role
	 */
	public void updateRole(Role role);

	/**
	 * 删除角色
	 * 
	 * @param role_id
	 */
	public void deleteRole(int role_id);

	/**
	 * 获取角色列表
	 * 
	 * @return
	 */
	public List<Role> getAllRoleList(@Param("page") Page<Role> page);

	/**
	 * 给用户授权一种角色
	 * 
	 * 
	 * @param user_id
	 *            role_id
	 */
	public void grantUserRole(@Param("user_id") int user_id, @Param("role_id") int role_id);

	/**
	 * 查询某角色的所有用户
	 * 
	 * @param role_id
	 * @return
	 */
	public List<User> getUserListByRoleId(@Param("role_id") int role_id, @Param("page") Page<User> page);
	/**
	 * 查询某角色的所有特定状态的用户
	 * 
	 * @param role_id status
	 * @return
	 */
	public List<User> getUserListByRoleIdAndStatus(@Param("role_id") int role_id,@Param("status") int status, @Param("page") Page<User> page);
	
	public List<User> getUserListByIsall(@Param("isall") String isall, @Param("page") Page<User> page);
	
	public List<User> getUserListByIsallAndIswin(@Param("isall") String isall,@Param("iswin") String iswin);	
	
	public List<User> getUserListByIsallAndIswinAndWinlevel(@Param("isall") String isall,@Param("iswin") String iswin,@Param("winlevel") String winlevel);	
	
	/**
	 * 查询用户的角色列表
	 * 
	 * @param user_id
	 * @return
	 */
	public List<Role> getRoleListByUserId(@Param("user_id") int user_id, @Param("page") Page<Role> page);

	/**
	 * 删除user的role
	 * 
	 * @param user_id
	 */
	public void deleteUserRoleByUserId(@Param("user_id") int user_id);

	public void updateStatus(User user);
	
	public void updateIsall(User user);
	
	public void updateIswin(User user);
	
	public int countWinner(Map<String, Object> map);
	
}
