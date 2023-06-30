package top.mengtech.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略统一响应注解定义
 */

@Target({ElementType.TYPE,ElementType.METHOD}) // 类、方法
@Retention(RetentionPolicy.RUNTIME) // 注解有效期：运行时
public @interface IgnoreResponseAdvice {

}
