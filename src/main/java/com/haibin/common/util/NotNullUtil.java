package com.haibin.common.util;

import com.haibin.common.annotation.NotNull;
import com.haibin.common.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 校验空工具
 *
 * @author haibin.tang
 * @create 2018-06-23 下午5:06
 **/
public class NotNullUtil {

    private static final String GET_PREFIX = "get";

    private static final String SET_PREFIX = "set";

    /**
     * 校验对象属性标注了{@link NotNull} 注解的不能为空
     *  note: 属性必须有get set方法, 且get方法不能有参数
     * @param target
     * @param <T>
     */
    public static <T> void check(T target) {
        try {
            Class clazz = target.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                NotNull notNull = field.getAnnotation(NotNull.class);
                if (notNull == null) {
                    continue;
                }
                Class<?> fieldType = field.getType();
                char[] fieldNameChars = field.getName().toCharArray();
                char fieldFirstCharToUpperCase = Character.toUpperCase(fieldNameChars[0]);
                fieldNameChars[0] = fieldFirstCharToUpperCase;
                final String methodName = GET_PREFIX + String.valueOf(fieldNameChars);
                Method method = clazz.getMethod(methodName, null);
                Object value = method.invoke(target, null);
                if (fieldType == String.class) {
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        throw new BizException(notNull.message());
                    }
                }
            }
        }catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
