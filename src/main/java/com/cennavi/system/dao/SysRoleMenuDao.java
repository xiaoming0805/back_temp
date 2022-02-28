package com.cennavi.system.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRoleMenu;
import com.cennavi.system.bean.SysRoleUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleMenuDao extends BaseDao<SysRoleMenu> {
    List<SysMenu> findMenusByRoleIds(Set<String> roleIds, Integer type);

    void delete(String id,String menuId);
}
