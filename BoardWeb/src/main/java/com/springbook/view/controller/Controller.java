package com.springbook.view.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.springbook.biz.user.UserVO;
import com.springbook.biz.user.impl.UserDAO;

public interface Controller {
	
	String handleRequest(HttpServletRequest request, HttpServletResponse response);
	
}
