package com.cennavi.system.service;



import com.cennavi.system.bean.SysRole;

import java.util.List;
import java.util.Map;

/**
 * @author zlt
 */
public interface ISysRoleUserService {
	void deleteUserRole(String userId, String roleId);
//
//	int saveUserRoles(Long userId, Long roleId);
//
	/**
	 * 根据用户id获取角色
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> findRolesByUserId(String userId);

	/**
	 * 根据用户ids 获取
	 *
	 * @param userIds
	 * @return
	 */
	List<Map<String,Object>> findRolesByUserIds(List<String> userIds);
}
