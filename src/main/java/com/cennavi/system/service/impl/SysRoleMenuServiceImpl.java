package com.cennavi.system.service.impl;

import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.dao.SysRoleMenuDao;
import com.cennavi.system.dao.SysUserRoleDao;
import com.cennavi.system.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author zlt
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl implements ISysRoleMenuService {
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
// 	@Resource
//	private SysRoleMenuMapper sysRoleMenuMapper;
//
//	@Override
//	public int save(Long roleId, Long menuId) {
//		return sysRoleMenuMapper.save(roleId, menuId);
//	}
//
	@Override
	public void delete(String roleId, String menuId) {
		 sysRoleMenuDao.delete(roleId, menuId);
	}

	@Override
	public List<SysMenu> findMenusByRoleIds(Set<String> roleIds, Integer type) {
		return sysRoleMenuDao.findMenusByRoleIds(roleIds, type);
	}
//
//	@Override
//	public List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes, Integer type) {
//		return sysRoleMenuMapper.findMenusByRoleCodes(roleCodes, type);
//	}
}
