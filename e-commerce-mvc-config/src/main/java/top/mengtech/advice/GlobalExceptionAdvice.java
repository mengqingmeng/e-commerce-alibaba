package top.mengtech.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.mengtech.vo.CommonResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice{

    @ExceptionHandler(value = Exception.class) // 异常拦截注解
    public CommonResponse<String>  handlerException(HttpServletRequest req,Exception ex){
        CommonResponse<String> response = new CommonResponse<>(-1,"error occurred");
        response.setData(ex.getMessage());

        log.error("server has error:[{}]",ex.getMessage(),ex);
        return response;
    }
}
