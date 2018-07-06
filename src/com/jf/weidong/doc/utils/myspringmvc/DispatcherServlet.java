package com.jf.weidong.doc.utils.myspringmvc;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/***
 * 自定义注解的核心处理器，负责调用目标业务方法处理用户请求
 *
 * @author weidong
 */
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = -187578654629000019L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            this.excute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    /**
     * 解析请求地址，并调用具体的处理请求【方法】
     */
    private void excute(HttpServletRequest request, HttpServletResponse response)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, ServletException, IOException {
        //将当前线程的HttpServletRequest、HttpServletResponse对象存入ThreadLocal中，以方便在Controller中使用
        RequestContextHolder.request.set(request);
        RequestContextHolder.response.set(response);

        String lastUrl = pareRequestURI(request);
        // 获取到对应的类
        Class<?> clazz = RequestURIMap.getUrlMap().get(lastUrl);
        if (clazz == null) {
            throw new RuntimeException("找不到【" + lastUrl + "】对应的类，请检查配置是否正确");
        }
        // 实例化对应的Servlet类
        Object classInstace = clazz.newInstance();
        Method m = null;// 记录目标方法
        // 请求路径和注解属性值相等，那么就是处理当前请求的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                String anoPath = method.getAnnotation(RequestMapping.class).value();
                if (anoPath != null && !"".equals(anoPath.trim()) && lastUrl.equals(anoPath.trim())) {
                    // 找到要执行的目标方法
                    m = method;
                    break;
                }
            }
        }
        if (m != null) {
            // System.out.println("找到执行的方法了" + m.getName());
            m.setAccessible(true);

            //获取方法的参数类型
            Class[] parameterTypes = m.getParameterTypes();

            //获取方法的参数名称
            List<String> methodParamNames = MethodUtils.getMethodParamNames(clazz, m);
            System.out.println("获取到方法的参数名称：" + methodParamNames);

            Object[] os = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class c = parameterTypes[i];
                //判断参数类型
                if (c.equals(HttpServletRequest.class)) {
                    //将参数存入os数组
                    os[i] = request;
                }else if (c.equals(HttpServletResponse.class)) {
                    os[i] = response;
                } else if (c.equals(String.class) || c.equals(Integer.class) || c.equals(Double.class) ||
                        c.equals(Long.class) || c.equals(Boolean.class) || c.equals(int.class) ||
                        c.equals(double.class) || c.equals(long.class) || c.equals(boolean.class)) {
                    //根据Controller方法中的参数名称获取请求地址的参数
                    String param = request.getParameter(methodParamNames.get(i));
                    if (param != null) {
                        try {
                            //判断参数的类型
                            if (c.equals(String.class)) {
                                os[i] = param.toString();
                            } else if (c.equals(Integer.class)) {
                                os[i] = Integer.parseInt(param);
                            } else if (c.equals(Double.class)) {
                                os[i] = Double.parseDouble(param);
                            } else if (c.equals(Long.class)) {
                                os[i] = Long.parseLong(param);
                            } else if (c.equals(Boolean.class)) {
                                os[i] = Boolean.parseBoolean(param);
                            } else if (c.equals(int.class)) {
                                os[i] = Integer.parseInt(param);
                            } else if (c.equals(double.class)) {
                                os[i] = Double.parseDouble(param);
                            } else if (c.equals(long.class)) {
                                os[i] = Long.parseLong(param);
                            } else if (c.equals(boolean.class)) {
                                os[i] = Boolean.parseBoolean(param);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            throw new RuntimeException(clazz.getName() + "类中的" + m.getName()
                                    + "方法参数【" + parameterTypes[i] + " " + methodParamNames.get(i) + "】与实际传入的值【" + param + "】"
                                    + "不匹配");
                        }
                    } else {
                        //如果参数是空的
                        //区分对象类型和基本类型
                        if (c.equals(String.class) || c.equals(Integer.class) || c.equals(Double.class)
                                || c.equals(Long.class) || c.equals(Boolean.class)) {
                            os[i] = null;
                        }
                        if (c.equals(int.class) || c.equals(double.class) || c.equals(long.class)) {
                            os[i] = 0;
                        }
                        if (c.equals(boolean.class)) {
                            os[i] = false;
                        }
                    }
                } else {
                    //如果不是基本类型和常见的包装类，那么表示有可能是自定义的类型
                    //将请求的参数注入到该类中
                    Object object = c.newInstance();
                    BeanUtils.copyProperties(object, request.getParameterMap());
                    os[i] = object;
                }
            }
            //获取到方法的返回值
            Object result = m.invoke(classInstace, os);
            if (result != null) {
                if (result instanceof ModelAndView) {
                    ModelAndView modelAndView = (ModelAndView) result;
                    //获取到参数
                    Map<Object, Object> p = modelAndView.getParams();
                    for (Object key : p.keySet()) {
                        //将参数存入域中
                        request.setAttribute(key + "", p.get(key));
                    }
                    if (modelAndView.getViewName() != null) {
                        //跳转页面
                        request.getRequestDispatcher(modelAndView.getViewName()).forward(request, response);
                    } else {
                        throw new RuntimeException("你返回了ModelAndView，但是没有设置视图，请调用setViewName()设置对应的视图");
                    }
                } else {
                    throw new RuntimeException("无法解析返回值类型");
                }
            }
        } else {
            throw new RuntimeException("在类" + clazz.getName() + "中找不到" + lastUrl + "方法，请检查方法是否存在");
        }
    }

    /**
     * 解析请求路径，获取到请求的路径，如【http://localhost/MySpringMVC/testServlet】--> 【/testServlet】
     */
    private String pareRequestURI(HttpServletRequest request) {
        String path = request.getContextPath();
        String requestUri = request.getRequestURI();
        String lasturl = requestUri.replaceFirst(path, "");
        lasturl = lasturl.substring(0, lasturl.lastIndexOf("."));
        return lasturl;
    }
}
