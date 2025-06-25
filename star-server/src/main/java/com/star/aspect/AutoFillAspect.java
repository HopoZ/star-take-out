package com.star.aspect;

import com.star.annotation.AutoFill;
import com.star.context.BaseContext;
import com.star.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //    切入点
    @Pointcut("execution(* com.star.mapper.*.*(..)) && @annotation(com.star.annotation.AutoFill)")
    public void autoFillPointCut() {

    }

    //前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        //获取拦截方法的属性
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autofill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autofill.value();
        //获取拦截方法的参数
        Object[] args=joinPoint.getArgs();
        if(args==null || args.length==0){
            return;
        }
        Object object =args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        try{
            if (operationType == OperationType.INSERT) {
                Method setCreateTime = object.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setCreateUser = object.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateTime = object.getClass().getDeclaredMethod("SetUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(object, now);
                setCreateUser.invoke(object,currentId);
                setUpdateTime.invoke(object,now);
                setUpdateUser.invoke(object,currentId);

            } else if(operationType==OperationType.UPDATE){
                Method setUpdateTime = object.getClass().getDeclaredMethod("SetUpdateTime", LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                setUpdateTime.invoke(object,now);
                setUpdateUser.invoke(object,currentId);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
