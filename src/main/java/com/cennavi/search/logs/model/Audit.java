package com.cennavi.search.logs.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 审计日志
 *
 * @author zlt
 * @date 2020/2/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
@Setter
@Getter
public class Audit {
    /**
     * 操作时间
     */
    private String timestamp;
    /**
     * 应用名
     */
    private String application_name;
    /**
     * 类名
     */
    private String class_name;
    /**
     * 方法名
     */
    private String method_name;
    /**
     * 用户id
     */
    private String user_id;
    /**
     * 用户名
     */
    private String user_name;
    /**
     * 租户id
     */
    private String client_id;
    /**
     * 操作信息
     */
    private String operation;
}
