package com.haibin.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codelogger.utils.CollectionUtils;
import org.codelogger.utils.MathUtils;
import org.codelogger.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * 封装
 *  字符串-->json对象
 *  json对象--> 字符串
 */
public class JsonUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> T json2Obj(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    public static <T> T json2Obj(InputStream src, Class<T> clazz) {
        if (src == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(src, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    public static <T> T json2Obj(String content, Class<T> clazzItem, Class... classes) {
        if (org.codelogger.utils.StringUtils.isBlank(content)) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(clazzItem, classes);
        try {
            return OBJECT_MAPPER.readValue(content, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    public static String obj2Json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Json序列化出错", e);
        }
    }

    public static void printObjectJsonDemo(Object obj) {
        fillValueToField(obj, true);
    }

    private static void fillValueToField(Object obj, Boolean isFirst) {
        if (obj == null) {
            logger.debug("null");
        } else {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String fieldName = declaredField.getName();
                if (!Modifier.isFinal(declaredField.getModifiers())) {
                    declaredField.setAccessible(true);
                    Type genericType = declaredField.getGenericType();
                    try {
                        Object value = declaredField.get(obj);
                        if (value == null) {
                            if (genericType == Integer.class || genericType == int.class) {
                                value = MathUtils.randomInt();
                            } else if (genericType == Long.class || genericType == long.class) {
                                value = MathUtils.randomLong();
                            } else if (genericType == Double.class || genericType == double.class) {
                                value = MathUtils.randomDouble();
                            } else if (genericType == String.class) {
                                value = "valueOf" + org.codelogger.utils.StringUtils.firstCharToUpperCase(fieldName);
                            } else if (genericType == Boolean.class
                                    || genericType == boolean.class) {
                                value = MathUtils.randomInt() % 2 == 0;
                            } else if (genericType == Byte.class || genericType == byte.class) {
                                value =
                                        String.valueOf(org.codelogger.utils.StringUtils.getRandomAlphabetic()).getBytes()[0];
                            } else if (genericType == Short.class || genericType == short.class) {
                                value = (short) MathUtils.randomInt(1, 9);
                            } else if (genericType == Character.class
                                    || genericType == char.class) {
                                value = org.codelogger.utils.StringUtils.getRandomAlphabetic();
                            } else if (genericType == BigDecimal.class) {
                                value = new BigDecimal(MathUtils.randomInt());
                            } else if (genericType == List.class) {
                                value = newArrayList();
                            } else if (genericType == Set.class) {
                                value = newHashSet();
                            } else if (genericType == Map.class) {
                                value = newHashMap();
                            } else if (genericType == Queue.class) {
                                value = new ConcurrentLinkedQueue<>();
                            }
                            if (value == null && isFirst) {
                                try {
                                    value = Class.forName(genericType.toString().split("\\s")[1]).newInstance();
                                    fillValueToField(value, false);
                                } catch (Exception ignored) {
                                    ignored.printStackTrace();
                                }
                            }
                        } else if (isFirst) {
                            try {
                                fillValueToField(value, false);
                            } catch (Exception ignored) {
                                ignored.printStackTrace();
                            }
                        }
                        if (value != null) {
                            declaredField.set(obj, value);
                        }
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }
    }

    public static String getString(JSONObject jsonObject, String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        } else if (jsonObject.has(key)) {
            return jsonObject.getString(key);
        } else {
            return "";
        }
    }

    public static void putContentToJsonObject(JSONObject target, JSONObject source) {
        if (source != null && CollectionUtils.isNotEmpty(source.keySet())) {
            if (target == null) {
                target = new JSONObject();
            }
            Iterator iterator = source.keySet().iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                target.put(key, source.get(key));
            }
        }
    }


    public static String getJSONStringValue(JSONObject jsonObject, String key, String filtrar) {
        Object value = jsonObject.get(key);
        if (value == null || "".equals(value.toString().trim()) || value.toString().trim().equals(filtrar.trim())) {
            return "";
        } else {
            return value.toString();
        }
    }

    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("KEY1", "a");
        jsonObject1.put("KEY2", "b");
        jsonObject1.put("KEY3", "c");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("KEY1", "c");
        jsonObject2.put("KEY2", "e");
        jsonObject2.put("KEY3", "f");
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("KEY1", null);
        jsonObject3.put("KEY2", null);
        jsonObject3.put("KEY3", null);


        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);
        System.out.println(jsonArray.toString());
//        sortJsonArray(jsonArray, "abc", "KEY1");
        System.out.println(jsonArray.toString());

    }
}
