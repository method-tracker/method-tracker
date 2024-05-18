package kr.co.yermi.tracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
public class MethodFlowAspect {

    private Set<String> visitedMethods = new HashSet<>();

    @Pointcut("execution(@kr.co.yermi.tracker.annotations.MethodFlow * *(..))")
    public void methodFlowPointcut() {}

    @AfterReturning("methodFlowPointcut()")
    public void afterAnnotatedMethodExecution(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        String methodName = joinPoint.getSignature().getName();

        // 최상위 메서드인 경우에만 실행
        if (!visitedMethods.contains(methodName)) {
            visitedMethods.add(methodName);
            List<String> invocationOrder = new ArrayList<>();
            addMethodInvocations(targetClass, methodName, invocationOrder, new HashSet<>());
            System.out.println("Method Invocation Order:");
            for (String invokedMethod : invocationOrder) {
                System.out.println("└─ " + invokedMethod);
            }
        }
    }

    private void addMethodInvocations(Class<?> clazz, String methodName, List<String> invocationOrder, Set<String> visitedMethods) {
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType().equals(kr.co.yermi.tracker.annotations.MethodFlow.class)) {
                            for (Method invokedMethod : method.getDeclaringClass().getDeclaredMethods()) {
                                if (!invokedMethod.getName().startsWith("ajc$") && visitedMethods.add(invokedMethod.getName())) {
                                    invocationOrder.add(invokedMethod.getName());
                                    addMethodInvocations(method.getDeclaringClass(), invokedMethod.getName(), invocationOrder, visitedMethods);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
