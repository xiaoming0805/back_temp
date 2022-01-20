package com.cennavi.modules;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务
 * Created by sunpengyan on 2019/4/2.
 */
@Component
public class SpringTimers {

    @Value("${job_is_excute}")
    private String jobIsExcute;

    /*@Autowired
    private UserService userService;*/

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

    /**
     * 每1分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void excuteJob(){
        //本地环境不执行定时任务
        if( jobIsExcute.equals("false")) {return;}
        System.out.println(format.format(new Date())+"任务开始执行");
        //调用执行任务的接口
    }

    /**
     * 每天凌晨1点执行一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void excuteJob2(){
        //本地环境不执行定时任务
        if( jobIsExcute.equals("false")) {return;}
        System.out.println(format.format(new Date())+"任务开始执行");
        //调用执行任务的接口
    }

}
