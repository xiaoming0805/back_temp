package com.cennavi.system.service;

import com.cennavi.system.bean.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * @author zlt
 */
public interface ISysRoleMenuService  {
//	int save(Long roleId, Long menuId);

	void delete(String roleId, String menuId);

	List<SysMenu> findMenusByRoleIds(Set<String> roleIds, Integer type);
//
//	List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes, Integer type);
}
