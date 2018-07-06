package com.jf.weidong.doc;

import java.util.Timer;
import java.util.TimerTask;

public class TestTime1 {
    public static void main(String[] args) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("我执行了");
            }
        };
        //从现在开始每隔1000ms 执行一次任务
        timer.schedule(timerTask, 0, 1000);
    }


}
