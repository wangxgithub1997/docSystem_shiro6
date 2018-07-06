package com.jf.weidong.doc.utils.myspringmvc;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    String viewName;//跳转到的视图名称（路径）

    Map<Object,Object> params = new HashMap<>();//存储数据

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void addParam(Object key, Object value) {
        params.put(key, value);
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<Object, Object> getParams() {
        return params;
    }



    public void setParams(Map<Object, Object> params) {
        this.params = params;
    }
}
