package com.cennavi.system.service.impl;

import com.cennavi.core.exception.GlobalException;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRoleMenu;
import com.cennavi.system.dao.SysMenuDao;
import com.cennavi.system.dao.SysRoleMenuDao;
import com.cennavi.system.service.ISysMenuService;
import com.cennavi.system.service.ISysRoleMenuService;

import com.cennavi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Service
public class SysMenuServiceImpl implements ISysMenuService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
// 	@Resource
//	private ISysRoleMenuService roleMenuService;

	@Override
	public void setMenuToRole(String roleId, Set<String> menuIds) {
		sysRoleMenuDao.delete(roleId, null);
		if (!CollectionUtils.isEmpty(menuIds)) {
			List<SysRoleMenu> roleMenus = new ArrayList<>(menuIds.size());
			menuIds.forEach(menuId -> roleMenus.add(new SysRoleMenu(roleId, menuId)));
			sysRoleMenuDao.batchSave(roleMenus);
		}
	}

	/**
	 * 角色菜单列表
	 * @param roleIds
	 * @return
	 */
	@Override
	public List<SysMenu> findByRoles(Set<String> roleIds) {
		return sysRoleMenuDao.findMenusByRoleIds(roleIds, null);
	}
//
//	/**
//	 * 角色菜单列表
//	 * @param roleIds 角色ids
//	 * @param roleIds 是否菜单
//	 * @return
//	 */
//	@Override
//	public List<SysMenu> findByRoles(Set<Long> roleIds, Integer type) {
//		return roleMenuService.findMenusByRoleIds(roleIds, type);
//	}
//
	@Override
	public List<SysMenu> findByRoleCodes(Set<String> roleCodes, Integer type) {
		return sysMenuDao.findMenusByRoleCodes(roleCodes, type);
	}

	@Override
	public void saveOrUpdate(SysMenu menu) {
		String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
		if(menu.getId()==null||"".equals(menu.getId())){
			menu.setCreate_time(dateStr);
			menu.setUpdate_time(dateStr);
			menu.setId(UUID.randomUUID().toString().replaceAll("-",""));
			sysMenuDao.saveMenus(menu);
		}else{
			sysMenuDao.updateMenus(menu);
		}
	}

	@Override
	public void removeById(String id) {
		List<Map<String,Object>>  l=sysMenuDao.findChildById(id);
		if(l.size()>0){
			throw new GlobalException(500,"请先删除子节点（逐级删除）");
		}
		sysMenuDao.removeById(id);
		sysRoleMenuDao.delete(null, id);
	}

	/**
     * 查询所有菜单
     */
	@Override
	public List<SysMenu> findAll() {
		return sysMenuDao.findAllMenus();
	}

    /**
     * 查询所有一级菜单
     */
	@Override
	public List<SysMenu> findOnes() {
        return sysMenuDao.findOnes();
	}


}
