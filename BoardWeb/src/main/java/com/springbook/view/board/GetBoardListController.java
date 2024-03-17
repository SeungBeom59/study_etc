package com.springbook.view.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.springbook.biz.board.BoardVO;
import com.springbook.biz.board.impl.BoardDAO;


public class GetBoardListController{

//	@RequestMapping("/getBoardList.do")
	public ModelAndView getBoardList(BoardVO vo, BoardDAO boardDAO, ModelAndView mav) {
		
		System.out.println("글 목록 검색 처리");
		
		mav.addObject("boardList", boardDAO.getBoardList(vo));	// Model 정보 저장
		mav.setViewName("getBoardList.jsp");	// View 정보 저장
		return mav;
		
	}

}
