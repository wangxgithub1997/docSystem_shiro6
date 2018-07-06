package com.jf.weidong.doc.utils.myspringmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 方便获取request 和 response
 */
public class RequestContextHolder {
    static ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();
    static ThreadLocal<HttpServletResponse> response = new ThreadLocal<>();
    public static HttpServletRequest getRequest() {
        return request.get();
    }

    public static HttpSession getSession(){
        return request.get().getSession();
    }


    public static HttpServletResponse getResponse() {
        return response.get();
    }

}
