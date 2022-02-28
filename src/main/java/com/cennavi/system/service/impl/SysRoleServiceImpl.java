package com.cennavi.system.service.impl;


import com.cennavi.core.exception.GlobalException;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.dao.SysRoleDao;
import com.cennavi.system.dao.SysRoleMenuDao;
import com.cennavi.system.dao.SysUserRoleDao;
import com.cennavi.system.service.ISysRoleService;
import com.cennavi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    private final static String LOCK_KEY_ROLECODE = "rolecode:";
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public void saveRole(SysRole sysRole) throws Exception {
        String roleCode = sysRole.getCode();
        List<Map<String,Object>> l=sysRoleDao.findByCode(roleCode);
        if(l.size()>0){
            throw new GlobalException(500,"角色code已存在");
        }
        sysRole.setId(UUID.randomUUID().toString().replaceAll("-",""));
        String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
        sysRole.setCreate_time(dateStr);
        sysRole.setUpdate_time(dateStr);
        sysRoleDao.save(sysRole,SysRole.class);
    }

    @Override
    public void deleteRole(String id) {
        sysRoleDao.deleteById(id);
        sysRoleMenuDao.delete(id, null);
        sysUserRoleDao.deleteUserRole(null, id);
    }

    @Override
    public WebPageResult<SysRole> findRoles(Map<String, Object> params) {
        Integer curPage = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        WebPageResult<SysRole> list = sysRoleDao.findList(curPage,limit, params);
        return list;
    }

    @Override
    public void saveOrUpdateRole(SysRole sysRole) throws Exception {
        if(StringUtils.isNotBlank(sysRole.getId())) {
            String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
            sysRole.setUpdate_time(dateStr);
            sysRoleDao.updateRole(sysRole);
        } else {
            this.saveRole(sysRole);
        }
    }

    @Override
    public List<SysRole> findAll() {
        return sysRoleDao.findAll();
    }
}
