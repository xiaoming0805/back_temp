package com.cennavi.system.dao.impl;

import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysRoleUser;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.dao.SysUserDao;
import com.cennavi.system.dao.SysUserRoleDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class SysUserRoleDaoImpl extends BaseDaoImpl<SysRoleUser> implements SysUserRoleDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<SysRole> findRolesByUserId(String userid) {
        List<Object> params = new ArrayList<>();
        String sql="select r.* from sys_role r " +
                " inner join sys_role_user ru on r.id = ru.role_id and ru.user_id = ?";
        params.add(userid);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysRole.class), params.toArray());
    }

    @Override
    public void deleteUserRole(String userId, String roleId) {
        List<Object> params = new ArrayList<>();
        String sql="delete from sys_role_user where 1=1";
        if(StringUtils.isNotBlank(userId)){
            sql+=" and user_id=?";
            params.add(userId);
        }
        if(StringUtils.isNotBlank(roleId)){
            sql+=" and role_id=?";
            params.add(roleId);
        }
        jdbcTemplate.update(sql,params.toArray());
    }

    @Override
    public List<Map<String,Object>> findRolesByUserIds(List<String> userIds) {
        String sql="select r.*,ru.user_id from sys_role_user ru inner join sys_role r on r.id = ru.role_id where  ru.user_id IN";
        List<Object> params = new ArrayList<>();
        String in="";
        for(String id:userIds){
            in+="'"+id+"',";
        }
        in=in.substring(0,in.length()-1);
        sql+="("+in+")";
        return jdbcTemplate.queryForList(sql, params.toArray());
    }
}
