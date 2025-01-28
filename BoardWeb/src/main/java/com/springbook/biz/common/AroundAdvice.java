package com.springbook.biz.common;

import com.springbook.biz.user.UserVO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.ast.Instanceof;
import org.h2.engine.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

//@Service
@Aspect
public class AroundAdvice {
	
	@Pointcut("execution(* com.springbook.biz..*Impl.*(..))")
	public void allPointcut() {}
	
	@Around("allPointcut()")
	public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
		
//		System.out.println("[Before]: 비즈니스 메소드 수행 전에 처리할 내용...");
		
		String method = pjp.getSignature().getName();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object returnObj = pjp.proceed();

		UserVO user = null;
		if(returnObj instanceof UserVO){
			user = (UserVO) returnObj;
		}

		stopWatch.stop();
		System.out.println(method + "() 메소드 수행에 걸린 시간: " + stopWatch.getTotalTimeMillis() + "(ms)초");

//		System.out.println("[After] 비즈니스 메소드 수행 후에 처리할 내용");
		
		return returnObj;
		
	}

}
