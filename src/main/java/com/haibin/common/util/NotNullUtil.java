package com.haibin.common.util;

import com.haibin.common.annotation.NotNull;
import com.haibin.common.exception.BizException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.codelogger.utils.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 校验空工具
 *
 * @author haibin.tang
 * @create 2018-06-23 下午5:06
 **/
public class NotNullUtil {

    private static final String GET_PREFIX = "get";

    /**
     * 校验对象属性标注了{@link NotNull} 注解的不能为空
     * note: 属性必须有get set方法, 且get方法不能有参数
     *
     * @param target
     * @param <T>
     */
    public static <T> void check(T target) {
        try {
            if (target == null) {
                throw new BizException("参数错误");
            }
            Class clazz = target.getClass();
            Field[] fields = getFields(clazz, new ArrayList<>());
            for (Field field : fields) {
                NotNull notNull = field.getAnnotation(NotNull.class);
                if (notNull == null) {
                    continue;
                }
                Class<?> fieldType = field.getType();
                final String methodName = GET_PREFIX + transFieldNameFirstToUpperCase(field.getName());
                Method method = clazz.getMethod(methodName, null);
                Object value = method.invoke(target, null);
                if (fieldType == String.class) {
                    if (value == null || StringUtils.isBlank(value.toString())) {
                        throw new BizException(notNull.message());
                    }
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换字段名首字母为大写
     *
     * @param fieldName
     * @return
     */
    private static String transFieldNameFirstToUpperCase(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        char fieldFirstCharToUpperCase = Character.toUpperCase(fieldNameChars[0]);
        fieldNameChars[0] = fieldFirstCharToUpperCase;
        return String.valueOf(fieldNameChars);
    }

    private static Field[] getFields(Class clazz, List<Field> container) {
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            container.addAll(Arrays.asList(fields));
        } else {
            return CollectionUtils.toArray(container);
        }
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            getFields(superClass, container);
        }
        return CollectionUtils.toArray(container);
    }
}
