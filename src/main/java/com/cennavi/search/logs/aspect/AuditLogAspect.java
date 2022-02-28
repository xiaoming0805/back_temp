package com.cennavi.search.logs.aspect;


import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.logs.properties.AuditLogProperties;
import com.cennavi.search.logs.service.IAuditService;
import com.cennavi.system.common.AuditLog;
import com.cennavi.system.common.TokenCache;
import com.cennavi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 审计日志切面
 *
 * @author zlt
 * @date 2020/2/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Slf4j
@Aspect
@Component
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class})
public class AuditLogAspect {
    @Value("${server.servlet.context-path}")
    private String applicationName;
    @Autowired
    private AuditLogProperties auditLogProperties;
    @Autowired
    private IAuditService auditService;
    @Autowired
    private TokenCache tokenCache;
//    public AuditLogAspect(AuditLogProperties auditLogProperties, IAuditService auditService) {
//        this.auditLogProperties = auditLogProperties;
//        this.auditService = auditService;
//    }

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Before("@within(auditLog) || @annotation(auditLog)")
    public void beforeMethod(JoinPoint joinPoint, AuditLog auditLog) {
        //判断功能是否开启
        Boolean flag=auditLogProperties.getEnabled();
        if (flag) {
            if (auditService == null) {
                log.warn("AuditLogAspect - auditService is null");
                return;
            }
            if (auditLog == null) {
                // 获取类上的注解
                auditLog = joinPoint.getTarget().getClass().getDeclaredAnnotation(AuditLog.class);
            }
            Audit audit = getAudit(auditLog, joinPoint);
            auditService.save(audit);
        }
    }

    /**
     * 解析spEL表达式
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEL);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for(int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }

    /**
     * 构建审计对象
     */
    private Audit getAudit(AuditLog auditLog, JoinPoint joinPoint) {
        Audit audit = new Audit();
        String dateStr = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(new Date());
        audit.setTimestamp(dateStr);
        audit.setApplication_name(applicationName);
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        audit.setClass_name(methodSignature.getDeclaringTypeName());
        audit.setMethod_name(methodSignature.getName());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Map<String,Object> map = getParameterMap(request);
        System.out.println("我是从Map参数获取的："+ Arrays.asList(map));

        String userId = null;
        String userName = null;
        String clientId = null;
        if(map.get("token")!=null){
            String token = map.get("token").toString();
            System.out.println(token);
            String user = tokenCache.getToken(token);
            System.out.println(user);
            JSONObject user_json=JSONObject.fromObject(user);
            userId=user_json.getString("id");
            userName=user_json.getString("username");
        }
        audit.setUser_id(userId);
        audit.setUser_name(userName);
        audit.setClient_id(clientId);
        String operation = auditLog.operation();
        if (operation.contains("#")) {
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            operation = getValBySpEL(operation, methodSignature, args);
        }
        audit.setOperation(operation);

        return audit;
    }

    /**
     * 获取所有请求参数，封装为map对象
     *
     * @return
     */
    public Map<String, Object> getParameterMap(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Enumeration<String> enumeration = request.getParameterNames();
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        StringBuilder stringBuilder = new StringBuilder();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            String keyValue = key + " : " + value + " ; ";
            stringBuilder.append(keyValue);
            parameterMap.put(key, value);
        }
        return parameterMap;
    }
}
