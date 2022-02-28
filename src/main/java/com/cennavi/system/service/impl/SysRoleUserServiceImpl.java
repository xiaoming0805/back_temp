package com.cennavi.system.service.impl;

import com.cennavi.system.bean.SysRole;
import com.cennavi.system.dao.SysUserDao;
import com.cennavi.system.dao.SysUserRoleDao;
import com.cennavi.system.service.ISysRoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zlt
 */
@Slf4j
@Service
public class SysRoleUserServiceImpl  implements ISysRoleUserService {
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
// 	@Resource
//	private SysUserRoleMapper sysUserRoleMapper;

	@Override
	public void deleteUserRole(String userId, String roleId) {
		sysUserRoleDao.deleteUserRole(userId, roleId);
	}
//
//	@Override
//	public int saveUserRoles(Long userId, Long roleId) {
//		return sysUserRoleMapper.saveUserRoles(userId, roleId);
//	}
//
	@Override
	public List<SysRole> findRolesByUserId(String userId) {
		return sysUserRoleDao.findRolesByUserId(userId);
	}

	@Override
	public List<Map<String,Object>> findRolesByUserIds(List<String> userIds) {
		return sysUserRoleDao.findRolesByUserIds(userIds);
	}
}
