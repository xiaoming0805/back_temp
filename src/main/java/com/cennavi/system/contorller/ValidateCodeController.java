package com.cennavi.system.contorller;


import com.cennavi.core.common.ResponseUtils;
import com.cennavi.core.common.ResultObj;
import com.cennavi.modules.sample.beans.SampleBean;
import com.cennavi.system.bean.SysUser;
import com.cennavi.system.common.TokenCache;
import com.cennavi.system.service.ISysUserService;
import com.cennavi.utils.DateUtils;
import com.cennavi.utils.RedisUtil;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import sun.java2d.pipe.AAShapePipe;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/validata")
public class ValidateCodeController extends ResponseUtils{
//    @Autowired
//    private IValidateCodeService validateCodeService;
    @Autowired
    private TokenCache tokenCache;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysUserService iSysUserService;
    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @GetMapping("/code")
    public void createCode(@RequestParam(value = "deviceId")  String deviceId, HttpServletResponse response) throws Exception {
        Assert.notNull(deviceId, "机器码不能为空");
        // 设置请求头为输出图片类型
        CaptchaUtil.setHeader(response);
        // 三个参数分别为宽、高、位数
        GifCaptcha gifCaptcha = new GifCaptcha(100, 35, 4);
        // 设置类型：字母数字混合
        gifCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 保存验证码
     //   validateCodeService.saveImageCode(deviceId, gifCaptcha.text().toLowerCase());

        redisUtil.setEx(deviceId, gifCaptcha.text().toLowerCase(),
                5, TimeUnit.MINUTES);
        // 输出图片流
        gifCaptcha.out(response.getOutputStream());
    }


    @PostMapping("/userlogin")
    public ResultObj token(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "validCode") String validCode,
                           @RequestParam(value = "deviceId") String deviceId)  {

        String text=redisUtil.get(deviceId);
        if(text==null||!validCode.toLowerCase().equals(text)){
            return error(500,"请输入正确的验证码");
        }
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> child_map=new HashMap<>();
        String uuid=UUID.randomUUID().toString();
        SysUser user=iSysUserService.getLoginAppUser(username,password);
        JSONObject users= JSONObject.fromObject(user);
        child_map.put("access_token", uuid);
        map.put("datas",child_map);
        tokenCache.setToken(uuid,users.toString(),2);
        String tk=tokenCache.getToken(uuid);
        System.out.println(tk);
        return success(map);
    }

    @GetMapping("/clients/all")
    public ResultObj clients_all()  {
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("id",1);
        map.put("clientId","webApp");
        map.put("clientName","PC端");
        map.put("resource_ids",null);
        map.put("client_secret","$2a$10$06msMGYRH8nrm4iVnKFNKOoddB8wOwymVhbUzw/d3ZixD7Nq8ot72");
        map.put("client_secret_str","webApp");
        map.put("web_server_redirect_uri","");
        map.put("scope","app");
        map.put("authorized_grant_types","authorization_code,password,refresh_token,client_credentials,implicit,password_code,openId,mobile_password");
        map.put("autoapprove",true);
        map.put("accessTokenValiditySeconds",3600);
        map.put("refreshTokenValiditySeconds",null);
        map.put("support_id_token",null);
        map.put("refresh_token_validity",null);
        list.add(map);
        return success(list);
    }

}
