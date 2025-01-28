package com.springbook.biz.common;

import org.aspectj.lang.JoinPoint;

public class Log4jAdvice {
	
	public void printLogging(JoinPoint jp , Exception exceptObj) {
		System.out.println("[공통 로그-Log4j] 비즈니스 로직 수행 전 동작");
	}

}
