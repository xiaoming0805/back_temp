package com.cennavi.system.dao.impl;

import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRoleMenu;
import com.cennavi.system.dao.SysRoleMenuDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
public class SysRoleMenuDaoImpl extends BaseDaoImpl<SysRoleMenu> implements SysRoleMenuDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<SysMenu> findMenusByRoleIds(Set<String> roleIds, Integer type) {
        List<Object> params = new ArrayList<>();
        String sql="select distinct t.* from sys_menu t " +
                " inner join sys_role_menu r on r.menu_id = t.id " +
                " where " +
                " r.role_id in ";
                String in="";
                for (String str : roleIds) {
                    in+="'"+str+"',";
                }
                in=in.substring(0,in.length()-1);
        sql+="("+in+")";
        if(type!=null && type!=0){
            sql += " and type=?";
            params.add(type);
        }
        sql+=" ORDER BY sort ASC";
        System.out.println(sql);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysMenu.class), params.toArray());
    }

    @Override
    public void delete(String role_id, String menuId) {
        List<Object> params = new ArrayList<>();
        String sql="delete from sys_role_menu where 1=1";
        if(StringUtils.isNotBlank(role_id)){
            sql+=" and role_id=?";
            params.add(role_id);
        }
        if(StringUtils.isNotBlank(menuId)){
            sql+=" and menu_id=?";
            params.add(menuId);
        }
        jdbcTemplate.update(sql,params.toArray());
    }
}
