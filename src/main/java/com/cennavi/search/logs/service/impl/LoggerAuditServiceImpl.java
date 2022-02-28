package com.cennavi.search.logs.service.impl;

import com.cennavi.search.common.ESPageResult;
import com.cennavi.search.logs.model.Audit;
import com.cennavi.search.logs.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 审计日志实现类-打印日志
 *
 * @author zlt
 * @date 2020/2/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "spring.audit-log.log-type", havingValue = "logger", matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {
    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}
     * 例子：2020-02-04 09:13:34.650|user-center|com.central.user.controller.SysUserController|saveOrUpdate|1|admin|webApp|新增用户:admin
     */
    @Override
    public void save(Audit audit) {
        log.debug(MSG_PATTERN
                , audit.getTimestamp()
                , audit.getApplication_name(), audit.getClass_name(), audit.getMethod_name()
                , audit.getUser_id(), audit.getUser_name(), audit.getClient_id()
                , audit.getOperation());
    }

    @Override
    public ESPageResult<Audit> getPage(Integer curPage, Integer limit, Map<String, Object> params) {
        return null;
    }
}
