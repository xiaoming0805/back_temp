package com.cennavi.system.service.impl;


import com.cennavi.core.exception.GlobalException;
import com.cennavi.modules.loginrsa.utils.RSAUtils;
import com.cennavi.modules.sample.dao.SampleDao;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysRoleUser;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.WebPage;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.dao.SysRoleMenuDao;
import com.cennavi.system.dao.SysUserDao;
import com.cennavi.system.dao.SysUserRoleDao;
import com.cennavi.system.service.ISysUserService;

import com.cennavi.utils.DateUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@Slf4j
@Service
public class SysUserServiceImpl  implements ISysUserService {
    public static final String BACKEND="BACKEND";
    public static final String APP="APP";
    public static final String DEF_USER_PASSWORD="123456";
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserRoleDao sysUserRoleDao;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private WebPage webPage;
    @Override
    public SysUser getLoginAppUser(String username,String password) {
        SysUser user=sysUserDao.getUser(username,RSAUtils.privateEncrypt(password));
        if(user==null){
            throw new GlobalException(500,"用户名或密码错误！");
        }
        //获取 角色
        List<SysRole> roles=sysUserRoleDao.findRolesByUserId(user.getId());
        user.setRoles(roles);
        Set<String> roleIds=new HashSet<>();
        for(SysRole map:roles){
            roleIds.add(map.getId());
        }
        //权限
        List<SysMenu> menus=sysRoleMenuDao.findMenusByRoleIds(roleIds, 2);//权限 2

        Set<String> permissions = menus.parallelStream().map(p -> p.getPath())
                .collect(Collectors.toSet());
        user.setPermissions(permissions);
        System.out.println(user);
        return user;
    }
//    private final static String LOCK_KEY_USERNAME = "username:";
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Resource
//    private ISysRoleUserService roleUserService;
//
//    @Resource
//    private SysRoleMenuMapper roleMenuMapper;
//
//    @Autowired
//    private DistributedLock lock;
//
//    @Override
//    public LoginAppUser findByUsername(String username) {
//        SysUser sysUser = this.selectByUsername(username);
//        return getLoginAppUser(sysUser);
//    }
//
//    @Override
//    public LoginAppUser findByOpenId(String username) {
//        SysUser sysUser = this.selectByOpenId(username);
//        return getLoginAppUser(sysUser);
//    }
//
//    @Override
//    public LoginAppUser findByMobile(String username) {
//        SysUser sysUser = this.selectByMobile(username);
//        return getLoginAppUser(sysUser);
//    }
//
//    @Override
//    public LoginAppUser getLoginAppUser(SysUser sysUser) {
//        if (sysUser != null) {
//            LoginAppUser loginAppUser = new LoginAppUser();
//            BeanUtils.copyProperties(sysUser, loginAppUser);
//
//            List<SysRole> sysRoles = roleUserService.findRolesByUserId(sysUser.getId());
//            // 设置角色
//            loginAppUser.setRoles(sysRoles);
//
//            if (!CollectionUtils.isEmpty(sysRoles)) {
//                Set<Long> roleIds = sysRoles.parallelStream().map(SuperEntity::getId).collect(Collectors.toSet());
//                List<SysMenu> menus = roleMenuMapper.findMenusByRoleIds(roleIds, CommonConstant.PERMISSION);
//                if (!CollectionUtils.isEmpty(menus)) {
//                    Set<String> permissions = menus.parallelStream().map(p -> p.getPath())
//                            .collect(Collectors.toSet());
//                    // 设置权限集合
//                    loginAppUser.setPermissions(permissions);
//                }
//            }
//            return loginAppUser;
//        }
//        return null;
//    }
//
//    /**
//     * 根据用户名查询用户
//     * @param username
//     * @return
//     */
//    @Override
//    public SysUser selectByUsername(String username) {
//        List<SysUser> users = baseMapper.selectList(
//                new QueryWrapper<SysUser>().eq("username", username)
//        );
//        return getUser(users);
//    }
//
//    /**
//     * 根据手机号查询用户
//     * @param mobile
//     * @return
//     */
//    @Override
//    public SysUser selectByMobile(String mobile) {
//        List<SysUser> users = baseMapper.selectList(
//                new QueryWrapper<SysUser>().eq("mobile", mobile)
//        );
//        return getUser(users);
//    }
//
//    /**
//     * 根据openId查询用户
//     * @param openId
//     * @return
//     */
//    @Override
//    public SysUser selectByOpenId(String openId) {
//        List<SysUser> users = baseMapper.selectList(
//                new QueryWrapper<SysUser>().eq("open_id", openId)
//        );
//        return getUser(users);
//    }
//
//    private SysUser getUser(List<SysUser> users) {
//        SysUser user = null;
//        if (users != null && !users.isEmpty()) {
//            user = users.get(0);
//        }
//        return user;
//    }
//
//    /**
//     * 给用户设置角色
//     */
//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void setRoleToUser(Long id, Set<Long> roleIds) {
//        SysUser sysUser = baseMapper.selectById(id);
//        if (sysUser == null) {
//            throw new IllegalArgumentException("用户不存在");
//        }
//
//        roleUserService.deleteUserRole(id, null);
//        if (!CollectionUtils.isEmpty(roleIds)) {
//            List<SysRoleUser> roleUsers = new ArrayList<>(roleIds.size());
//            roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(id, roleId)));
//            roleUserService.saveBatch(roleUsers);
//        }
//    }

    @Override
    public void updatePassword(String id, String oldPassword, String newPassword) {
        SysUser sysUser = sysUserDao.selectById(id);
        if (StringUtils.isNotBlank(oldPassword)) {
            if (!sysUser.getPassword().equals(RSAUtils.privateEncrypt(oldPassword))) {
                throw new GlobalException(500,"旧密码错误");
            }
        }
        if (StringUtils.isBlank(newPassword)) {
            newPassword = RSAUtils.privateEncrypt(DEF_USER_PASSWORD);
        }else{
            newPassword = RSAUtils.privateEncrypt(newPassword);
        }

        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(newPassword);
        sysUserDao.updatePawd(user);
    }

    @Override
    public WebPageResult<SysUser> findUsers(Map<String, Object> params) {
        Integer curPage = MapUtils.getInteger(params, "page");
        Integer limit = MapUtils.getInteger(params, "limit");
        WebPageResult<SysUser> pagelist = sysUserDao.findList(curPage,limit, params);
        List<SysUser> list = pagelist.getData();
        if (pagelist.getCount() > 0) {
            List<String> userIds = list.stream().map(SysUser::getId).collect(Collectors.toList());
            List<Map<String,Object>> sysRoles = sysUserRoleDao.findRolesByUserIds(userIds);
            list.forEach(u -> u.setRoles(webPage.toBean(sysRoles.stream().filter(r -> !ObjectUtils.notEqual(u.getId(), r.get("user_id").toString()  )).collect(Collectors.toList()),SysRole.class)));
        }
        return pagelist;
    }
//
//    @Override
//    public List<SysRole> findRolesByUserId(Long userId) {
//        return roleUserService.findRolesByUserId(userId);
//    }

    @Override
    public void updateEnabled(Map<String, Object> params) {
        String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
        String id = MapUtils.getString(params, "id");
        Integer enabled = MapUtils.getInteger(params, "enabled");
        SysUser appUser=new SysUser();
        appUser.setEnabled(enabled);
        appUser.setUpdate_time(dateStr);
        appUser.setId(id);
        sysUserDao.updateUserEnabled(appUser);
    }

    @Override
    public void saveOrUpdateUser(SysUser sysUser){
        String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
        String username = sysUser.getUsername();
        List<Map<String,Object>>  l=sysUserDao.checkUserName(username);
        if (sysUser.getId() == null||"".equals(sysUser.getId())) {
            if(l.size()>0){
                 throw new GlobalException(500,username+"已存在");
            }else{
                if (StringUtils.isBlank(sysUser.getType())) {
                    sysUser.setType(BACKEND);
                }
                sysUser.setPassword(RSAUtils.privateEncrypt(DEF_USER_PASSWORD));
                sysUser.setEnabled(1);
                sysUser.setId(UUID.randomUUID().toString().replaceAll("-",""));
                sysUser.setCreate_time(dateStr);
                sysUser.setUpdate_time(dateStr);
                sysUser.setIsDel(0);
                sysUserDao.saveUser(sysUser);
            }
        }else{//修改
            if(l.size()>0){
                if(!l.get(0).get("id").toString().equals(sysUser.getId())){//同一个账号 可以保存
                    throw new GlobalException(500,username+"已存在");
                }
            }
            sysUser.setUpdate_time(dateStr);
            sysUserDao.updateUser(sysUser);
        }

        //更新角色
        if (StringUtils.isNotBlank(sysUser.getRoleId())) {
            sysUserRoleDao.deleteUserRole(sysUser.getId(), null);
            List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<SysRoleUser> roleUsers = new ArrayList<>(roleIds.size());
                roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(sysUser.getId(), roleId.toString())));
                sysUserRoleDao.batchSave(roleUsers);
            }
        }
    }

    @Override
    public void delUser(String id) {
        sysUserRoleDao.deleteUserRole(id, null);
        sysUserDao.deleteById(id);
    }
//
//    @Override
//    public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
//        List<SysUserExcel> sysUserExcels = new ArrayList<>();
//        List<SysUser> list = baseMapper.findList(new Page<>(1, -1), params);
//
//        for (SysUser sysUser : list) {
//            SysUserExcel sysUserExcel = new SysUserExcel();
//            BeanUtils.copyProperties(sysUser, sysUserExcel);
//            sysUserExcels.add(sysUserExcel);
//        }
//        return sysUserExcels;
//    }
}