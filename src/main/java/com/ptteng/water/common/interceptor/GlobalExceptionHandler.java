package com.ptteng.water.common.interceptor;



import com.ptteng.water.system.pojo.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *  全局异常拦截器 拦截所有exception
 * at 2018/10/5 12:16
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final int ERROR_CODE=-10000;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<String> jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        e.printStackTrace();
        Response<String> r = new Response<>();
        r.setMessage("系统错误");
        r.setCode(ERROR_CODE);
        r.setData(req.getRequestURL().toString());
        return r;
    }


}
