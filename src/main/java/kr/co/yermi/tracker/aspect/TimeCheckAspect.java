package kr.co.yermi.tracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TimeCheckAspect {

    private long startTime; // 클래스의 멤버 변수로 startTime 선언

    @Before("execution(* *.*(..)) && @annotation(kr.co.yermi.tracker.annotations.TimeCheck)")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        // 메서드 실행 전에 시작 시간 기록
        startTime = System.currentTimeMillis(); // startTime 변수에 현재 시간 기록
        System.out.println("Before 동작 " + joinPoint.getSignature().getName());
    }

    @After("execution(* kr.co.yermi.tracker.*.*(..)) && @annotation(kr.co.yermi.tracker.annotations.TimeCheck)")
    public void afterMethodExecution(JoinPoint joinPoint) {
        // 메서드 실행 후에 종료 시간 기록 및 실행 시간 출력
        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        long executionTime = endTime - startTime; // 실행 시간 계산
        System.out.println("Method " + joinPoint.getSignature().getName() + " executed in " + executionTime + " milliseconds");
    }
}
