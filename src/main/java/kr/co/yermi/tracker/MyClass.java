package kr.co.yermi.tracker;

import kr.co.yermi.tracker.annotations.MethodFlow;
import kr.co.yermi.tracker.annotations.TimeCheck;

public class MyClass {
	@TimeCheck
    public void myMethod() {
        System.out.println("Executing myMethod");
    }

	@MethodFlow
    public void anotherMethod() {
        System.out.println("Executing anotherMethod");
//        methodA();
    }
//	@MethodFlow
	private void methodA() {
		methodB();
	}
//	@MethodFlow
	private void methodB() {
		methodC();
	}
//	@MethodFlow
	private void methodC() {
		methodD();
	}
//	@MethodFlow
	private void methodD() {
		System.out.println("the end");
	}
}
