//package com.springbook.view.user;
//
//import com.springbook.biz.user.UserVO;
//import com.springbook.biz.user.impl.UserDAO;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpSession;
//
//@Controller
//public class LoginController {
//
//	@RequestMapping(value="/login.do" , method = RequestMethod.GET)
//	public String loginView(@ModelAttribute("user")UserVO vo) {
//		System.out.println("로그인 화면으로 이동");
//		vo.setId("test");
//		vo.setPassword("test123");
//		return "login.jsp";
//	}
//
//	@RequestMapping(value="/login.do" , method = RequestMethod.POST)
//	public String login(UserVO vo, UserDAO userDAO , HttpSession session) {
//		System.out.println("로그인 처리");
//		System.out.println("로그인 아이디: " + vo.getId() + " , 로그인 패스워드: " + vo.getPassword());
//
//		if(vo.getId() == null || vo.getId().equals("")) {
//			throw new IllegalArgumentException("아이디는 반드시 입력해야 합니다.");
//		}
//
//		UserVO user = new UserVO();
//		user = userDAO.getUser(vo);
//		if(user != null) {
//			session.setAttribute("userName", user.getName());
//			return "redirect:getBoardList.do";
//		}
//		else {
//			return "login.jsp";
//		}
//	}
//}
