package com.cennavi.system.dao.impl;

import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.dao.SysMenuDao;
import com.cennavi.system.dao.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository
public class SysMenuDaoImpl extends BaseDaoImpl<SysMenu> implements SysMenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SysMenu> findMenusByRoleCodes(Set<String> roleCodes, Integer type) {
        List<Object> params=new ArrayList<>();
        String sql="select distinct t.* from sys_menu t "+
                " inner join sys_role_menu r on r.menu_id = t.id "+
                " inner join sys_role rl on rl.id = r.role_id "+
                " where 1=1 ";
        String in="";
        for(String str:roleCodes){
            in+="'"+str+"',";
        }
        in=in.substring(0,in.length()-1);
        sql+=  " and rl.code in ("+in+")";
        if(type!=null&&type!=0){
            sql+=" and t.type=? ";
            params.add(type);
        }
        sql+=" and t.hidden = 0 "+
        " ORDER BY sort ASC";
        System.out.println(sql);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysMenu.class), params.toArray());
    }

    @Override
    public List<SysMenu> findAllMenus() {
        List<Object> params=new ArrayList<>();
        String sql="select t.* from sys_menu t order by sort";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysMenu.class), params.toArray());
    }

    @Override
    public List<SysMenu> findOnes() {
        List<Object> params=new ArrayList<>();
        String sql="select t.* from sys_menu t where type=? order by sort";
        params.add(1);
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysMenu.class), params.toArray());
    }

    @Override
    public void saveMenus(SysMenu menu) {
        String sql="insert into sys_menu(css,hidden,id,name,parent_id,path,path_method,sort,type,url,create_time,update_time,tenant_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,menu.getCss(),menu.getHidden(),menu.getId(),menu.getName(),menu.getParent_id(),menu.getPath(),menu.getPath_method(),menu.getSort(),menu.getType(),menu.getUrl(),menu.getCreate_time(),menu.getUpdate_time(),menu.getTenant_id());
    }

    @Override
    public void updateMenus(SysMenu menu) {
        String sql="update sys_menu set css=?,hidden=?,name=?,parent_id=?,path=?,path_method=?,sort=?,type=?,url=?,update_time=? where id=?";
        jdbcTemplate.update(sql,menu.getCss(),menu.getHidden(),menu.getName(),menu.getParent_id(),menu.getPath(),menu.getPath_method(),menu.getSort(),menu.getType(),menu.getUrl(), menu.getUpdate_time(),menu.getId());
    }

    @Override
    public void removeById(String id) {
        String sql="delete from sys_menu where id=?";
        jdbcTemplate.update(sql,id);
    }

    @Override
    public List<Map<String, Object>> findChildById(String id) {
        String sql="select * from sys_menu where parent_id=?";
        return jdbcTemplate.queryForList(sql,id);
    }
}
