package com.haibin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注EXCEL字段信息
 *
 * @author haibin.tang
 * @create 2018-07-03 下午4:33
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XlsColumn {
    /**
     * 当前字段对应excel的列数 从0开始
     * @return
     */
    int index();
}
