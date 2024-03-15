<%-- <%@ page import="com.springbook.biz.board.impl.BoardDAO" %>
<%@ page import="com.springbook.biz.board.BoardVO" %> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
/* 	// 1. DB에 검색할 게시글 번호 추출.
	String seq = request.getParameter("seq");
	
	// 2. DB 연동 처리
	BoardVO vo = new BoardVO();
	vo.setSeq(Integer.parseInt(seq));
	
	BoardDAO boardDAO = new BoardDAO();
	BoardVO board = boardDAO.getBoard(vo);

	// 3. 응답 화면 구성 */
	
	// 세션에 저장된 게시글 정보를 꺼낸다.
	/* BoardVO board = (BoardVO) session.getAttribute("board"); */
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<%-- <title><%= board.getSeq() %> 번째 글 상세</title> --%>
<title>${board.seq} 번째 글 상세</title>
</head>
<body>
	<center>
		<h1>글 상세</h1>
		<!-- <a href="logout_proc.jsp">Log-out</a> -->
		<a href="logout.do">Log-out</a>
		<hr>
		<!-- <form action="updateBoard_proc.jsp" method="post"> -->
		<form action="updateBoard.do" method="post">
			<%-- <input name="seq" type="hidden" value="<%= board.getSeq() %>" /> --%>
			<input name="seq" type="hidden" value="${board.seq}" />
			<table border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td bgcolor="orange" width="70">제목</td>
					<%-- <td align="left"><input name="title" type="text" value="<%= board.getTitle() %>"></td> --%>
					<td align="left"><input name="title" type="text" value="${board.title}"></td>
				</tr>
				<tr>
					<td bgcolor="orange">작성자</td>
					<%-- <td align="left"><%= board.getWriter() %></td> --%>
					<td align="left">${board.writer}</td>
				</tr>
				<tr>
					<td bgcolor="orange">내용</td>
					<td align="left">
						<textarea name="content" cols="40" rows="10">
							<%-- <%= board.getContent() %> --%>
							${board.content}
						</textarea>
					</td>
				</tr>
				<tr>
					<td bgcolor="orange">등록일</td>
					<%-- <td align="left"><%= board.getRegDate() %></td> --%>
					<td align="left">${board.regDate}</td>
				</tr>
				<tr>
					<td bgcolor="orange">조회수</td>
					<%-- <td align="left"><%= board.getCnt() %></td> --%>
					<td align="left">${board.cnt}</td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="글 수정" /></td>
				</tr>
			</table>
		</form>
		
		<hr>
		<a href="insertBoard.jsp">글 등록</a>&nbsp;&nbsp;&nbsp;
		<%-- <a href="deleteBoard_proc.jsp?seq=<%= board.getSeq() %>">글 삭제</a>&nbsp;&nbsp;&nbsp; --%>
		<%-- <a href="deleteBoard.do?seq=<%= board.getSeq() %>">글 삭제</a>&nbsp;&nbsp;&nbsp; --%>
		<a href="deleteBoard.do?seq=${board.seq}">글 삭제</a>&nbsp;&nbsp;&nbsp;
		<a href="getBoardList.do">글 목록</a>		
	</center>
</body>
</html>