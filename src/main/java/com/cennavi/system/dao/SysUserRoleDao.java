package com.cennavi.system.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysRoleUser;
import com.cennavi.system.bean.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserRoleDao extends BaseDao<SysRoleUser> {
    List<SysRole> findRolesByUserId(String userid);

    void deleteUserRole(String userId, String roleId);

    List<Map<String,Object>> findRolesByUserIds(List<String> userIds);
}
