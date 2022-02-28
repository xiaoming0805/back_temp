package com.cennavi.system.contorller;


import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.system.bean.SysMenu;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.TokenCache;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.service.ISysMenuService;
import com.vividsolutions.jts.util.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@RestController
@Api(tags = "菜单模块api")
@Slf4j
@RequestMapping("/menus")
public class SysMenuController extends ResponseUtils {
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private ISysMenuService menuService;
//
//    /**
//     * 两层循环实现建树
//     *
//     * @param sysMenus
//     * @return
//     */
//    public static List<SysMenu> treeBuilder(List<SysMenu> sysMenus) {
//        List<SysMenu> menus = new ArrayList<>();
//        for (SysMenu sysMenu : sysMenus) {
//            if (ObjectUtil.equal(-1L, sysMenu.getParentId())) {
//                menus.add(sysMenu);
//            }
//            for (SysMenu menu : sysMenus) {
//                if (menu.getParentId().equals(sysMenu.getId())) {
//                    if (sysMenu.getSubMenus() == null) {
//                        sysMenu.setSubMenus(new ArrayList<>());
//                    }
//                    sysMenu.getSubMenus().add(menu);
//                }
//            }
//        }
//        return menus;
//    }

    /**
     * 删除菜单
     *
     * @param id
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/deleteMenu")
    public ResultObj delete(@RequestParam String id) {
        menuService.removeById(id);
        return success();
    }

    @ApiOperation(value = "根据roleId获取对应的菜单")
    @GetMapping("/menusTree")
    public List<Map<String, Object>> findMenusByRoleId(@RequestParam(value = "roleId") String roleId) {
        Set<String> roleIds = new HashSet<>();
        roleIds.add(roleId);
        //获取该角色对应的菜单
        List<SysMenu> roleMenus = menuService.findByRoles(roleIds);
        //全部的菜单列表
        List<SysMenu> allMenus = menuService.findAll();
        List<Map<String, Object>> authTrees = new ArrayList<>();

        Map<String, SysMenu> roleMenusMap = roleMenus.stream().collect(Collectors.toMap(SysMenu::getId, SysMenu -> SysMenu));

        for (SysMenu sysMenu : allMenus) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", sysMenu.getId());
            authTree.put("name", sysMenu.getName());
            authTree.put("pId", sysMenu.getTenant_id());
            authTree.put("open", true);
            authTree.put("checked", false);
            if (roleMenusMap.get(sysMenu.getId()) != null) {
                authTree.put("checked", true);
            }
            authTrees.add(authTree);
        }
        return authTrees;
    }
//
//    @ApiOperation(value = "根据roleCodes获取对应的权限")
//    @SuppressWarnings("unchecked")
//    @Cacheable(value = "menu", key ="#roleCodes")
//    @GetMapping("/{roleCodes}")
//    public List<SysMenu> findMenuByRoles(@PathVariable String roleCodes) {
//        List<SysMenu> result = null;
//        if (StringUtils.isNotEmpty(roleCodes)) {
//            Set<String> roleSet = (Set<String>)Convert.toCollection(HashSet.class, String.class, roleCodes);
//            result = menuService.findByRoleCodes(roleSet, CommonConstant.PERMISSION);
//        }
//        return result;
//    }

    /**
     * 给角色分配菜单
     */
    @ApiOperation(value = "角色分配菜单")
    @PostMapping("/granted")
    public ResultObj setMenuToRole(@RequestBody SysMenu sysMenu) {
        menuService.setMenuToRole(sysMenu.getRoleId(), sysMenu.getMenuIds());
        return success();
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/findAlls")
    public WebPageResult<SysMenu> findAlls() {
        List<SysMenu> list = menuService.findAll();
        return WebPageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
    }

    @ApiOperation(value = "获取菜单以及顶级菜单")
    @GetMapping("/findOnes")
    public WebPageResult<SysMenu> findOnes() {
        List<SysMenu> list = menuService.findOnes();
        return WebPageResult.<SysMenu>builder().data(list).code(0).count((long) list.size()).build();
    }

    /**
     * 添加菜单 或者 更新
     *
     * @param menu
     * @return
     */
    @ApiOperation(value = "新增菜单")
    @PostMapping("saveOrUpdate")
    public ResultObj saveOrUpdate(@RequestBody SysMenu menu,@RequestParam(value = "tenantId") String tenantId) {
        menu.setTenant_id(tenantId);
        menuService.saveOrUpdate(menu);
        return  success();
    }

    /**
     * 当前登录用户的菜单
     *
     * @return
     */
    @GetMapping("/current")
    @ApiOperation(value = "查询当前用户菜单")
    public List<SysMenu> findMyMenu(String token) {
        String user = tokenCache.getToken(token);
        JSONObject object=JSONObject.fromObject(user);
        JSONArray array=object.getJSONArray("roles");
        Set<String> set=new HashSet<>();
        for(int i=0;i<array.size();i++){
            set.add(array.getJSONObject(i).getString("code"));
        }
        List<SysMenu> menus = menuService.findByRoleCodes(set, 1);
        return treeBuilder(menus);
    }
    /**
     * 两层循环实现建树
     *
     * @param sysMenus
     * @return
     */
    public static List<SysMenu> treeBuilder(List<SysMenu> sysMenus) {
        List<SysMenu> menus = new ArrayList<>();
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getParent_id().equals("-1")) {
                menus.add(sysMenu);
            }
            for (SysMenu menu : sysMenus) {
                if (menu.getParent_id().equals(sysMenu.getId())) {
                    if (sysMenu.getSubMenus() == null) {
                        sysMenu.setSubMenus(new ArrayList<>());
                    }
                    sysMenu.getSubMenus().add(menu);
                }
            }
        }
        return menus;
    }
}
