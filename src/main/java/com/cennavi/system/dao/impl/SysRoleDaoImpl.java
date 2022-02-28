package com.cennavi.system.dao.impl;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPage;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.dao.SysRoleDao;
import com.cennavi.system.dao.SysUserDao;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class SysRoleDaoImpl extends BaseDaoImpl<SysRole> implements SysRoleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebPage webPage;
    @Override
    public WebPageResult<SysRole> findList(Integer page,Integer limit ,Map<String, Object> params) {
        List<Object> find_params=new ArrayList<>();
        String base_sql="select * from sys_role t where 1=1 ";
        Object searchKey=params.get("searchKey");
        Object searchValue=params.get("searchValue");
        if(searchKey!=null&&searchValue!=null&&StringUtils.isNotBlank(searchKey.toString())&&StringUtils.isNotBlank(searchValue.toString())) {
            base_sql += "and t."+searchKey.toString()+" like CONCAT ('%',?,'%')";
            find_params.add(searchValue.toString());
        }
        base_sql+=" order by t.create_time desc";
        return webPage.getPageList(jdbcTemplate,base_sql,find_params,page,limit,SysRole.class);
    }

    @Override
    public List<Map<String, Object>> findByCode(String code) {
        String sql="select * from sys_role where code=?";
        return jdbcTemplate.queryForList(sql,code);
    }

    @Override
    public void updateRole(SysRole sysRole) {
        String sql="update sys_role set name=?,update_time=?  where id=?";
        jdbcTemplate.update(sql,sysRole.getName(),sysRole.getUpdate_time(),sysRole.getId());
    }

    @Override
    public void deleteById(String id) {
        String sql="delete from sys_role where id=?";
        jdbcTemplate.update(sql,id);
    }
}
