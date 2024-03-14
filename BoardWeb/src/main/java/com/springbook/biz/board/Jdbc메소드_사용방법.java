//package com.springbook.biz.board;
//
//public class Jdbc메소드_사용방법 {
//	
//	// update() 메소드 사용
//	// 1. sql 구문에 설정된 ? 수만큼 값들 차례대로 나열하는 방식
//	public void updateBoard(BoardVO vo) {
//		String BOARD_UPDATE = "update board set title=? , content=? where seq=?";
//		int cnt = jdbcTemplate.update(BOARD_UPDATE, vo.getTitle(), vo.getContent(), vo.getSeq());
//		System.out.println(cnt + "건 데이터 수정");
//	}
//	
//	// 2. Object 배열 객체에 sql 구문에 설정된 ? 수만큼의 값들을 미리 세팅하여 배열 객체를 두번째 인자로 쓰기
//	public void updateBoard(BoardVO vo) {
//		String BOARD_UPDATE = "update board set title=? , content=? where seq=?";
//		Object[] args = {vo.getTitle(), vo.getContent(), vo.getSeq()};
//		int cnt = jdbcTemplate.update(BOARD_UPDATE, args);
//		System.out.println(cnt + "건 데이터 수정");
//	}
//	
//	// queryForInt() 메소드
//	// 전체 게시글 수 조회
//	public void getBoardTotalCount(BoardVO vo) {
//		String BOARD_TOT_COUNT = "select count(*) from board";
//		int count = jdbcTemplate.queryForInt(BOARD_TOT_COUNT);
//		System.out.println("전체 게시글 수: " + count + "건");
//	}
//	
//	// queryForObject() 메소드
//	// 꼭 주의 해야하는 것이, 2개 이상의 검색 결과 또는 결과 없음의 경우 예외를 발생시킨다.
//	// select 구문으로 받은 결과값을 자바 객체로 리턴 받기 위해서는 RowMapper 객체를 사용하여 매핑해줘야만 한다!
//	// com.springbook.biz.board.impl.BoardRowMapper 참고 
//	// 글 상세 조회
//	public BoardVO getBoard(BoardVO vo) {
//		String BOARD_GET = "select * from board where seq=?";
//		Object[] args = {vo.getSeq()};
//		return jdbcTemplate.queryForObject(BOARD_GET, args, new BoardRowMapper());
//	}
//	
//	// query() 메소드
//	public List<BoardVO> getBoardList(BoardVO vo){
//		String BOARD_LIST = "select * from board order by seq desc";
//		return jdbcTemplate.query(BOARD_LIST, new BoardRowMapper());
//	}
//}
