package com.cennavi.core.config.schedule;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 自用简便版
 */
@Controller
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private SpringScheduledCronRepository cronRepository;

    /**
     * 查看任务列表
     */
    @RequestMapping("/taskList")
    @ResponseBody
    public List<SpringScheduledCron> taskList() {
        List<SpringScheduledCron> list = cronRepository.findAll();
        return list;
    }

    /**
     * 编辑任务cron表达式
     */
    @ResponseBody
    @RequestMapping("/editTaskCron")
    public String editTaskCron(String cron_id, String cron_key, String cron_expression, String task_explain, String status, String cron_name) {
        try {
            if (StringUtils.isNotBlank(cron_expression)) {
                new CronTrigger(cron_expression);
            }
        } catch (Exception e) {
            return "cron表达式有误:" + e.getMessage();
        }
        try {
            if (StringUtils.isNotBlank(cron_key)) {
                context.getBean(Class.forName(cron_key));
            }
        } catch (Exception e) {
            return "完整类名 有误:" + e.getMessage();
        }
        if ("".equals(cron_id) || cron_id == null) {
            status = "2";
            cronRepository.insertTask(cron_key, cron_expression, task_explain, status, cron_name);
        } else {
            cronRepository.updatetTask(cron_id, cron_key, cron_expression, task_explain, status, cron_name);
        }
        return "ok";
    }

    /**
     * 执行定时任务
     */
    @ResponseBody
    @RequestMapping("/runTaskCron")
    public String runTaskCron(String cronKey) throws Exception {
        ((ScheduledOfTask) context.getBean(Class.forName(cronKey))).execute();
        return null;
    }

    @RequestMapping("/toUpdateTask")
    @ResponseBody
    public Map<String, Object> toUpdateTask(String cron_id) {
        Map<String, Object> map = cronRepository.toUpdate(cron_id);
        return map;
    }

    @RequestMapping("/deleteTask")
    @ResponseBody
    public String deleteTask(String cron_id) {
        cronRepository.deleteTask(cron_id);
        return null;
    }

}
