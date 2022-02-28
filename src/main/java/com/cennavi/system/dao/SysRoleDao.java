package com.cennavi.system.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.common.WebPageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SysRoleDao extends BaseDao<SysRole> {
    WebPageResult<SysRole> findList(Integer page, Integer limit, Map<String, Object> params);

    List<SysRole> findAll();

    List<Map<String,Object>> findByCode(String code);

    void updateRole(SysRole sysRole);

    void deleteById(String id);
}
