package kr.co.yermi.tracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Aspect
public class MethodFlowAspect {

    private ThreadLocal<Stack<String>> methodStack = ThreadLocal.withInitial(Stack::new);
    private ThreadLocal<List<String>> methodInvocationList = ThreadLocal.withInitial(ArrayList::new);

    @Pointcut("execution(* *.*(..)) && @annotation(kr.co.yermi.tracker.annotations.MethodFlow)")
    public void methodFlowPointcut() {}

    @Pointcut("execution(@kr.co.yermi.tracker.annotations.MethodFlow * *(..))")
    public void topLevelMethod() {}

    @AfterReturning("methodFlowPointcut()")
    public void afterAnnotatedMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Stack<String> stack = methodStack.get();

        // 현재 메서드를 호출 스택에 추가
        stack.push(methodName);

        // 최상위 메서드에서 호출될 때마다 호출 스택을 초기화하고, 현재 호출된 메서드들을 저장
        if (stack.size() == 1) {
            List<String> invocationList = methodInvocationList.get();
            invocationList.addAll(stack);
            methodInvocationList.set(invocationList);
            stack.clear();
        }
    }

    // 최상위 메서드에서 호출된 모든 메서드 반환
    public List<String> getMethodInvocationList() {
        return methodInvocationList.get();
    }

    @Before("methodFlowPointcut() && topLevelMethod()")
    public void beforeAnnotatedMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Stack<String> stack = methodStack.get();
        stack.push(methodName);
        printMethodInvocationOrder(stack);
    }

    private void printMethodInvocationOrder(Stack<String> stack) {
        System.out.println("Method Invocation Order:");
        for (int i = 0; i < stack.size(); i++) {
            String methodName = stack.get(i);
            StringBuilder indent = new StringBuilder();
            for (int j = 0; j < i; j++) {
                indent.append("    ");
            }
            System.out.println(indent.toString() + "└─ " + methodName);
        }
    }
}
