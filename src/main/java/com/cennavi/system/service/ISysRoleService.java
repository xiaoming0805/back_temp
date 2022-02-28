package com.cennavi.system.service;

import com.cennavi.system.bean.SysRole;
import com.cennavi.system.common.WebPageResult;

import java.util.List;
import java.util.Map;

/**
 * @author zlt
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public interface ISysRoleService  {
	void saveRole(SysRole sysRole) throws Exception;

	void deleteRole(String id);

	/**
	 * 角色列表
	 * @param params
	 * @return
	 */
	WebPageResult<SysRole> findRoles(Map<String, Object> params);
	/**
	 * 新增或更新角色
	 * @param sysRole
	 * @return Result
	 */
	void saveOrUpdateRole(SysRole sysRole) throws Exception;

	/**
	 * 查询所有角色
	 * @return
	 */
	List<SysRole> findAll();
}
