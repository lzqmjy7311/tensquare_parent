package com.tensquare.gathering.web.common;

import com.itcast.common.constants.StatusCode;
import com.itcast.common.dto.ResultDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类(通知类)
 */
//@ControllerAdvice
//组合注解，相当于@ControllerAdvice+@ResponseBody
@RestControllerAdvice
public class BaseExceptionHandler {

//    @RequestBody
    //增强的方法：异常通知处理
    //@ExceptionHandler(Exception.class)
	@ExceptionHandler
    public ResultDTO error(Exception e){
        e.printStackTrace();
        return new ResultDTO(false, StatusCode.ERROR,e.getMessage());
    }
}
