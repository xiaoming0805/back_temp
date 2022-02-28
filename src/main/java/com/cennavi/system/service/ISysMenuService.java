package com.cennavi.system.service;

import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.common.WebPageResult;

import java.util.List;
import java.util.Set;

/**
 * @author zlt
 */
public interface ISysMenuService  {
	/**
	 * 查询所有菜单
	 */
	List<SysMenu> findAll();


	/**
	 * 查询所有一级菜单
	 */
	List<SysMenu> findOnes();

	/**
	 * 角色分配菜单
	 * @param roleId
	 * @param menuIds
	 */
	void setMenuToRole(String roleId, Set<String> menuIds);

	/**
	 * 角色菜单列表
	 * @param roleIds 角色ids
	 * @return
	 */
	List<SysMenu> findByRoles(Set<String> roleIds);
//
//	/**
//	 * 角色菜单列表
//	 * @param roleIds 角色ids
//	 * @param roleIds 是否菜单
//	 * @return
//	 */
//	List<SysMenu> findByRoles(Set<Long> roleIds, Integer type);
//
	/**
	 * 角色菜单列表
	 * @param roleCodes
	 * @return
	 */
	List<SysMenu> findByRoleCodes(Set<String> roleCodes, Integer type);

	void saveOrUpdate(SysMenu menu);


	void removeById(String id);
}
