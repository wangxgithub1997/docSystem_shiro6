package com.jf.weidong.doc.utils.myspringmvc;

/*
import com.jf.weidong.doc.check.TestJob;*/

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Method;
import java.util.Set;

public class ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       /* new TestJob().start();*/
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                borrowManageService.checkBorrowInfo();
//            }
//        };
//        //从现在开始每隔1000ms 执行一次任务
//        timer.schedule(timerTask, 0, 5000);
        try {
            System.out.println("---初始化开始---");
            // 这里需要进行扫描，扫描添加了 Controller 注解的类
            String basePackage = "com.jf.weidong.doc.controller";
            Set<Class<?>> classes = ScanClassUtil.getClasses(basePackage);
            for (Class<?> clazz : classes) {
                // 判断是否包含该注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    // 如果有该注解，那么取到注解的值，存入Map集合中
                    Method[] methods = clazz.getDeclaredMethods();
                    // System.out.println(methods.toString());
                    for (Method method : methods) {
                        // 判断方法是否添加了注解
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String url = rm.value();
                            // URL不能重复
                            if (!RequestURIMap.isExist(url)) {
                                System.out.println(clazz.getName() + "类中扫描到方法：" + method.getName() + "  url 是：" + url);
                                RequestURIMap.put(url, clazz);
                            } else {
                                // System.out.println("重复 了");
                                throw new RuntimeException("在【" + clazz.getName() + "】类中发现：【" + url + "】，和类：【"
                                        + RequestURIMap.getUrlMap().get(url) + "】中的【" + url + "】有重复，请检查代码");
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
