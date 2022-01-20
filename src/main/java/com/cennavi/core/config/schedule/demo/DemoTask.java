package com.cennavi.core.config.schedule.demo;

import com.cennavi.core.config.schedule.ScheduledOfTask;
import org.springframework.stereotype.Component;

@Component
public class DemoTask implements ScheduledOfTask {
    private int i;
    @Override
    public void execute() {
        System.out.println("Thread_id="+ Thread.currentThread().getId()+"  执行第:"+ ++i+"次");
    }
}
