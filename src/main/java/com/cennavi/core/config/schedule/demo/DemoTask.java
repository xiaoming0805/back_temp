package com.cennavi.core.config.schedule.demo;

import com.cennavi.core.config.schedule.ScheduledOfTask;
import com.cennavi.core.config.schedule.SpringScheduledCronRepository;
import com.cennavi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DemoTask implements ScheduledOfTask {
    private int i;
    @Autowired
    private SpringScheduledCronRepository cronRepository;

    /**
     * 业务执行方法
     *
     * @return
     */
    public String work() {
        String dateStr = DateUtils.DateFormatUnit.DATE.getDateStr(DateUtils.addDays(new Date(), -30));
        String mes = cronRepository.deleteLog(dateStr);
        return mes;//例如: 执行成功/本次同步卡口数据 2000条，过车数据100000条。
    }

    @Override
    public void execute() {
        long startTime = System.currentTimeMillis();
        Date begindate = new Date();
        String begintime = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(begindate);
        String cron_key = this.getClass().getName();
        System.out.println(cron_key);
        String logs = "";
        String state = "1";
        try {
            logs = work();
            state = "1";
        } catch (Exception e) {
            state = "2";
            logs = e.getMessage();
        } finally {
            Date enddate = new Date();
            String endtime = DateUtils.DateFormatUnit.DATE_TIME.getDateStr(enddate);
            long endTime = System.currentTimeMillis();
            String timer = ((double) endTime - (double) startTime) / 1000 + "s.";
            System.out.println("Thread_id=" + Thread.currentThread().getId() + "  执行第:" + ++i + "次");
            cronRepository.insertLog(cron_key, begintime, endtime, state, logs, timer);
        }
    }

}
