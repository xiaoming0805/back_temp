package com.cennavi.system.dao.impl;

import com.cennavi.core.common.dao.impl.BaseDaoImpl;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPage;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.dao.SysUserDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class SysUserDaoImpl extends BaseDaoImpl<SysUser> implements SysUserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private WebPage webPage;

    public SysUser getUser(String username,String password) {
        List<Object> params = new ArrayList<>();
        String sql="select * from sys_user where username=? ";
        params.add(username);
       // params.add(password);
        List<SysUser> list=jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysUser .class), params.toArray());
        if(list.size()==1){
            return list.get(0);
        }
        return null;
    }

    @Override
    public WebPageResult<SysUser> findList(Integer page, Integer limit, Map<String, Object> params) {
        List<Object> find_params=new ArrayList<>();
        String base_sql="select * from sys_user t where 1=1 ";
        Object searchKey=params.get("searchKey");
        Object searchValue=params.get("searchValue");
        if(searchKey!=null&&searchValue!=null&&StringUtils.isNotBlank(searchKey.toString())&&StringUtils.isNotBlank(searchValue.toString())) {
            base_sql += "and t."+searchKey.toString()+" like CONCAT ('%',?,'%')";
            find_params.add(searchValue.toString());
        }
        base_sql+=" order by t.create_time desc";
        return webPage.getPageList(jdbcTemplate,base_sql,find_params,page,limit, SysUser.class);
    }

    @Override
    public List<Map<String, Object>> checkUserName(String username) {
        String sql="select * from sys_user where username=?";
        return jdbcTemplate.queryForList(sql,username);
    }

    @Override
    public void updateUser(SysUser u) {
        String sql="update sys_user set username=?,nickname=?,mobile=?,sex=? where id=?";
        jdbcTemplate.update(sql,u.getUsername(),u.getNickname(),u.getMobile(),u.getSex(),u.getId());
    }

    @Override
    public void saveUser(SysUser u) {
        String sql="insert into sys_user (id,username,password,nickname,mobile,sex,enabled,type,create_time,update_time,is_del) " +
                " values(?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,u.getId(),u.getUsername(),u.getPassword(),u.getNickname(),u.getMobile(),u.getSex(),u.getEnabled(),u.getType(),u.getCreate_time(),u.getUpdate_time(),u.getIsDel());
    }

    @Override
    public void updateUserEnabled(SysUser u) {
        String sql="update sys_user set enabled=?,update_time=? where id=?";
        jdbcTemplate.update(sql,u.getEnabled(),u.getUpdate_time(),u.getId());
    }

    @Override
    public SysUser selectById(String id) {
        String sql="select * from sys_user where id=?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        List<SysUser> list=jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SysUser .class), params.toArray());
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updatePawd(SysUser u) {
        String sql="update sys_user set password=? where id=?";
        jdbcTemplate.update(sql,u.getPassword(),u.getId());
    }

    @Override
    public void deleteById(String id) {
        String sql="delete from sys_user  where id=?";
        jdbcTemplate.update(sql,id);
    }
}
