package com.haibin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注方法参数需要打日志
 *
 * @author haibin.tang
 * @create 2018-05-27 下午5:41
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OutLog {
    /**
     * 需要打印的参数索引
     * @return
     */
    int[] index() default 0;
    /**
     * 参数前的描述
     * @return
     */
    String[] prefix() default {"", "", "", "", "", "", "", "", "", ""};
}
