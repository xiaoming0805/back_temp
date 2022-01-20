package com.cennavi.core.config.schedule;

public interface ScheduledOfTask extends Runnable {
    /**
     * 定时任务方法
     */
    void execute();

    /**
     * 实现控制定时任务启用或禁用的功能
     */
    @Override
    default void run() {
        SpringScheduledCronRepository repository = SpringUtils.getBean(SpringScheduledCronRepository.class);
        SpringScheduledCron scheduledCron = repository.findByCronKey(this.getClass().getName());
        if ("2".equals(scheduledCron.getStatus())) {
            return;
        }
        //配置中的定时任务开关
        if (com.cennavi.core.config.SpringContextUtil.getProperty("job_is_excute").equals("false")) {//获取测试数据
            return;
        }
        execute();
    }
}
