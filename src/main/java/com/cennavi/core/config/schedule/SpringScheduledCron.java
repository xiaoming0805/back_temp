package com.cennavi.core.config.schedule;

import com.cennavi.core.common.MyTable;
import lombok.Data;

import java.util.Date;
@Data
//调用公司封装方法 解析表名 使用
@MyTable("spring_scheduled_cron")
public class SpringScheduledCron {
    private String cron_id;
    private String cron_key;
    private String cron_expression;
    private String task_explain;
    private String status;
    private String cron_name;
    private Date createtime;
}
