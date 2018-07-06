package com.jf.weidong.doc.check;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestJob {

    public void start(){
        //创建一个SchedulerFactory对象
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            //通过SchedulerFactory对象获取任务调度器
            Scheduler scheduler = schedulerFactory.getScheduler();
            //定义任务的触发条件
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger1",
                    "CronTriggerGroup")
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(10)
                            .repeatForever())//一直执行
                    .startNow().build();
            //创建一个job：定义一个执行的任务
            JobDetail job = JobBuilder.newJob(MyJob.class)
                    .withIdentity("job1", "group1")
                    .build();
            //把job和触发器注册到调度器中
            scheduler.scheduleJob(job, trigger);
            //启动调度器
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
