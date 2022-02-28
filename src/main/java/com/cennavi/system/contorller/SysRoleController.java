package com.cennavi.system.contorller;


import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.system.bean.SysRole;
import com.cennavi.system.common.WebPageResult;
import com.cennavi.system.common.TokenCache;
import com.cennavi.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * 角色管理
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色模块api")
public class SysRoleController extends ResponseUtils {
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private TokenCache tokenCache;
    /**
     * 后台管理查询角色
     * @param params
     * @return
     */
    @ApiOperation(value = "后台管理查询角色")
    @GetMapping("/roles")
    public WebPageResult<SysRole> findRoles(@RequestParam Map<String, Object> params) {
        return sysRoleService.findRoles(params);
    }

    /**
     * 用户管理查询所有角色
     * @return
     */
    @ApiOperation(value = "后台管理查询角色")
    @GetMapping("/allRoles")
    public ResultObj<List<SysRole>> findAll() {
        List<SysRole> result = sysRoleService.findAll();
        return success(result);
    }

    /**
     * 角色新增或者更新
     *
     * @param sysRole
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public ResultObj saveOrUpdate(@RequestBody SysRole sysRole,@RequestParam(value = "tenantId") String tenantId) throws Exception {
        sysRole.setTenant_id(tenantId);
        sysRoleService.saveOrUpdateRole(sysRole);
        return success();
    }

    /**
     * 后台管理删除角色
     * delete /role/1
     *
     * @param id
     */
    @ApiOperation(value = "后台管理删除角色")
    @DeleteMapping("/deleteRole")
    public ResultObj deleteRole(@RequestParam(value = "id") String id) {
        if (id.equals("1")) {
            return error(500,"管理员不可以删除!");
        }
        sysRoleService.deleteRole(id);
        return success();
    }
}
