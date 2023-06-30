package top.mengtech.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.mengtech.annotation.IgnoreResponseAdvice;
import top.mengtech.vo.CommonResponse;

import java.util.Objects;

/**
 * 实现统一响应，对相应的数据进行封装
 */
@RestControllerAdvice(value = "top.mengtech") // 针对特定包有效
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 是否对响应进行处理
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果类或方法被IgnoreResponseAdvice注解修饰，返回false，不处理响应结果。
        return !(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class) ||
                Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class));
    }

    // 在响应之前
    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        CommonResponse<Object> response = new CommonResponse<>(0,"");
        if(null == o){
            return response;
        }else if (o instanceof  CommonResponse){
            response = (CommonResponse<Object>) o;
        }else{
            response.setData(o);
        }
        return response;
    }
}
