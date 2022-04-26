package com.cennavi.modules.loginrsa.service.impl;

import com.cennavi.modules.loginrsa.dao.LoginDao;
import com.cennavi.modules.loginrsa.service.LoginService;
import com.cennavi.modules.loginrsa.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 样例service
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;


    @Override
    public boolean login(String username, String password) {
        //密码是通过RSA公钥加密过的，目前给前端使用公钥  MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7qdEg98tYYRtSkOk9AxEETZIKksEPoc9pPy0SWYP3hFJL2LwNNLH+mX+8dbCFtedH09gMw3gLDuwQePrQU59070rMmGK7nkarOYjTZF2IYdHK4hQRMlEQQIrUK6MQCZrlXbi5SNA5cmxidhPmduT2QFsrfdbmtKjPvXa/1lku8wIDAQAB
        String pwd = RSAUtils.privateDecrypt(password);//将密码进行解密
        List<Map<String,Object>> list = loginDao.getUser(username,password);
        boolean flag = false;
        if(list.size() > 0){
            String od_ps=list.get(0).get("password").toString();
            String od_pwd= RSAUtils.privateDecrypt(od_ps);//将密码进行解密
            if(pwd.equals(od_pwd)){
                flag = true;//校验通过，可能需要生成token返回前端，项目中自行定制
            }
        }
        return flag;
    }
}
