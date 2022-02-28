package com.cennavi.system.service;



import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zlt
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public interface ISysUserService  {
//	/**
//	 * 获取UserDetails对象
//	 * @param username
//	 * @return
//	 */
//	LoginAppUser findByUsername(String username);
//
//	LoginAppUser findByOpenId(String username);
//
//	LoginAppUser findByMobile(String username);
//
	/**
	 * 通过SysUser 转换为 LoginAppUser，把roles和permissions也查询出来
	 * @return
	 */
	SysUser getLoginAppUser(String username,String password);
//
//	/**
//	 * 根据用户名查询用户
//	 * @param username
//	 * @return
//	 */
//	SysUser selectByUsername(String username);
//	/**
//	 * 根据手机号查询用户
//	 * @param mobile
//	 * @return
//	 */
//	SysUser selectByMobile(String mobile);
//	/**
//	 * 根据openId查询用户
//	 * @param openId
//	 * @return
//	 */
//	SysUser selectByOpenId(String openId);
//
//	/**
//	 * 用户分配角色
//	 * @param id
//	 * @param roleIds
//	 */
//	void setRoleToUser(Long id, Set<Long> roleIds);

	/**
	 * 更新密码
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	void updatePassword(String id, String oldPassword, String newPassword);

	/**
	 * 用户列表
	 * @param params
	 * @return
	 */
	WebPageResult<SysUser> findUsers(Map<String, Object> params);


//	/**
//	 * 用户角色列表
//	 * @param userId
//	 * @return
//	 */
//	List<SysRole> findRolesByUserId(Long userId);

	/**
	 * 状态变更
	 * @param params
	 * @return
	 */
	void updateEnabled(Map<String, Object> params);
//
//	/**
//	 * 查询全部用户
//	 * @param params
//	 * @return
//	 */
//	List<SysUserExcel> findAllUsers(Map<String, Object> params);

	void saveOrUpdateUser(SysUser sysUser) ;

	/**
	 * 删除用户
	 */
	void delUser(String id);
}
