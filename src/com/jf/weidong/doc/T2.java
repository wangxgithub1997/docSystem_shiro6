package com.jf.weidong.doc;

import com.jf.weidong.doc.utils.myspringmvc.MethodUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class T2 {
    public static void main(String[] args) {
        try {
            Class t2 = T2.class;
            Object o = t2.newInstance();
            Method m1 = t2.getMethod("m1", String.class,String.class);
            Object msg = m1.invoke(o, "李四","参数2");


            List<String> methodParamNames = MethodUtils.getMethodParamNames(t2, m1);
            System.out.println("获取到的参数名："+methodParamNames);


            System.out.println("返回值"+msg);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void m1(String name,String password) {
        System.out.println("被调用了" + name);
        //return "我是返回值";
    }
}
