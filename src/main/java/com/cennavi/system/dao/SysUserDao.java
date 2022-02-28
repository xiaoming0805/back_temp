package com.cennavi.system.dao;

import com.cennavi.core.common.dao.BaseDao;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPageResult;

import java.util.List;
import java.util.Map;

public interface SysUserDao extends BaseDao<SysUser> {
    SysUser getUser(String username,String password);

    WebPageResult<SysUser> findList(Integer page, Integer limit, Map<String, Object> params);

    List<Map<String,Object>> checkUserName(String username);

    void updateUser(SysUser sysUser);

    void saveUser(SysUser sysUser);

    void updateUserEnabled(SysUser sysUser);

    SysUser selectById(String id);

    void updatePawd(SysUser u);

    void deleteById(String id);
}
