package com.cennavi.system.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysMenuDao extends BaseDao<SysMenu> {
    List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes, Integer type);

    List<SysMenu> findAllMenus();

    List<SysMenu> findOnes();

    void saveMenus(SysMenu menu);

    void updateMenus(SysMenu menu);

    void removeById(String id);

    List<Map<String,Object>> findChildById(String id);
}
