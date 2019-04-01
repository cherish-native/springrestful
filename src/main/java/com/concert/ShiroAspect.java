package com.concert;


import com.annotation.Aouth;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2018/11/8
 * 声明一个切面,切面包含 切点和通知
 */
@Aspect
public class ShiroAspect {

    @Pointcut("execution(* com.controller..*.*(..)) && @annotation(com.annotation.Aouth) ")
    public void performance(){}

    @Around("performance()")
    public Map<String,Object> aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws ClassNotFoundException {
        Map<String,Object> result_map = new HashMap<>();
        String targetName = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Class targetClass = Class.forName(targetName);
        try{
            Method[] methods = targetClass.getMethods();
            Optional<Method>  methodOptional = Arrays.stream(methods).filter(t -> t.getName().equals(methodName)).findFirst();
            if(methodOptional.isPresent()){
                Aouth aouth = methodOptional.get().getAnnotation(Aouth.class);
                if(aouth.code().equals("1201")){
                    result_map = (Map<String,Object>)proceedingJoinPoint.proceed();
                }
            }else{
                throw new RuntimeException("没有操作权限");
            }
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
        return result_map;
    }
}
