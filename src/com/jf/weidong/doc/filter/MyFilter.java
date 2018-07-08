package com.jf.weidong.doc.filter;

import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter extends CharacterEncodingFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("ccontext:"+request.getContextPath());
        System.out.println(request.getServletPath());
        System.out.println(request.getPathInfo());
        System.out.println("requesturi"+request.getRequestURI());
        super.doFilterInternal(request, response, filterChain);
    }
}
