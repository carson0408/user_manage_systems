package com.carson.cachedemo.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * ClassName ScheduleTask
 *
 * @author zhanghangfeng5
 * @description
 * @Version V1.0
 * @createTime
 */
@Component
public class ScheduleTask {


    @Scheduled(cron="0/3 * * * * ?")
    public void scheduleDo(){
        System.out.println(new Date(System.currentTimeMillis()));
    }
}
