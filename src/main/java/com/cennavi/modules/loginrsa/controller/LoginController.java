package com.cennavi.modules.loginrsa.controller;

import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.loginrsa.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"用户登录RSA非对称加密"},value = "login")
@RestController
@RequestMapping("/login")
public class LoginController extends ResponseUtils {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;


    /**
     * 说明
     * 统计类接口查询-1
     * 场景：使用多表查询或者 sum count 出来的数据 表中没有相应的字段
     * 例如多表查询且使用 jdbcTemplate 返回了 List<Map<String,Object>> 或者 Map<String,Object> 类型的数据，生成api标准接口文档时
     * 可以 使用 ResponseUtils 类中 toBean 方法 转化为类 并且返回
     * 缺点：写大量的 VO 类文件 影响效率
     * 如果不想写大量的类文件。可以参考例子，允许简便写法
     */
    @ApiOperation(value = "用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", required=true, dataType = "String", name = "username", value = "用户名", example = "阎长虹"),
            @ApiImplicitParam(paramType = "query", required=true,dataType = "String", name = "password", value = "使用RSA公钥加密后的密码", example = "2021-12-23 14:33:27"),
    })
    @GetMapping("/useLogin")
    /**
     * 前端用crypto-js进行加密，
     * npm i jsencrypt，
     * 然后页面头引入import JSEncrypt from 'jsencrypt';
     * const encrypt = new JSEncrypt();
     * encrypt.setPublicKey('你的公钥'); MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7qdEg98tYYRtSkOk9AxEETZIKksEPoc9pPy0SWYP3hFJL2LwNNLH+mX+8dbCFtedH09gMw3gLDuwQePrQU59070rMmGK7nkarOYjTZF2IYdHK4hQRMlEQQIrUK6MQCZrlXbi5SNA5cmxidhPmduT2QFsrfdbmtKjPvXa/1lku8wIDAQAB
     * password = encrypt.encrypt(‘你的密码’);// 加密后的字符串
     */
    public ResultObj<String> useLogin(@RequestParam(value = "username") String username,String password)  {
        boolean flag = loginService.login(username,password);
        if(flag)
            return success();
        else
            return error("用户名或密码错误!");
    }





}
