package com.jf.weidong.doc.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jf.weidong.doc.utils.Result;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class AopException {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ModelAndView handlerException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        //判断是否权限异常
        if(ex instanceof UnauthorizedException){
            return new ModelAndView("error/nopass");
        }
        //判断ajax请求
        if(request.getHeader("X-Requested-With") != null
               && request.getHeader("X-Requested-With")
                .equalsIgnoreCase("XMLHttpRequest")){
            Result result;
           if(ex instanceof BusinessException){
                BusinessException businessException = (BusinessException) ex;
                result = new Result(businessException.getCode(),businessException.getMessage());
            }else{
                result = new Result(-1,"发送了未知异常");
            }
           //result  --- > json
            ObjectMapper objectMapper = new ObjectMapper();
           String json = objectMapper.writeValueAsString(result);
           response.getWriter().write(json);
           return null;
        }else{
            return new ModelAndView("error/500");
        }
    }
}
