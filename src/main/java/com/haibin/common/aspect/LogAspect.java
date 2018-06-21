package com.haibin.common.aspect;

import com.haibin.common.annotation.OutLog;
import com.haibin.common.util.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 日志打印切面
 *
 * @author haibin.tang
 * @create 2018-05-27 下午5:46
 **/
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 该方法配置为前置通知
     *
     * @param joinPoint
     * @throws Throwable
     */
    public void log(JoinPoint joinPoint) {
        if (!LOG.isDebugEnabled()) {
            return;
        }
        try {
            Method targetMethod = getTargetMethod(joinPoint);
            if (targetMethod == null) {
                LOG.debug("未获取到目标方法");
                return;
            }
            OutLog outLog = targetMethod.getAnnotation(OutLog.class);
            if (outLog == null) {
                return;
            }
            int[] index = outLog.index();
            final String[] desc = outLog.prefix();
            Object[] args = joinPoint.getArgs();
            int prefixIndex = 0;
            final String reference = getReference(targetMethod);
            for (int i : index) {
                LOG.debug("{}:{}:{}", reference, desc[prefixIndex++], JsonUtil.obj2Json(args[i]));
            }
        } catch (Throwable e) {
            LOG.error("解析OutLog注解出错", e);
        }
    }

    /**
     * 获取Class简单名称与方法名
     *
     * @param method
     * @return
     */
    private String getReference(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    /**
     * 根据JoinPoint解析出Method
     *
     * @param point
     * @return
     */
    private Method getTargetMethod(JoinPoint point) {
        Object[] args = point.getArgs();
        Class<?>[] argTypes = new Class[point.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = point.getTarget().getClass()
                    .getMethod(point.getSignature().getName(), argTypes);
        } catch (Exception e) {
            LOG.error("解析日志目标方法出错", e);
        }
        return method;
    }
}
