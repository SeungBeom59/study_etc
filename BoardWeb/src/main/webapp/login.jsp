<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8" content="text/html" http-equiv="Content-Type">
<title><spring:message code="message.user.login.title"/></title>
</head>
<body>
	<center>
		<!-- <h1>로그인</h1> -->
		<h1><spring:message code="message.user.login.title"/></h1>
		<a href="login.do?lang=en"><spring:message code="message.user.login.language.en"/></a>&nbsp;&nbsp;
		<a href="login.do?lang=ko"><spring:message code="message.user.login.language.ko"/></a>&nbsp;&nbsp;		
		<hr>
		<form action="login.do" method="post">
		<form action="login_proc.jsp" method="post">
			<table border="1" cellpadding="0" cellspacing="0">
				<tr>
					<!-- <td bgcolor="orange">아이디</td> -->
					<td bgcolor="orange"><spring:message code="message.user.login.id"/></td>
					<td><input type="text" name="id" value="${user.id}"/></td>
				</tr>
				<tr>
					<!-- <td bgcolor="orange">비밀번호</td> -->
					<td bgcolor="orange"><spring:message code="message.user.login.password"/></td>
					<td><input type="password" name="password" value="${user.password}" /></td>
				</tr>
				<tr>
					<td colspan="2" allign="center">
						<input type="submit" value="<spring:message code="message.user.login.loginBtn"/>" />
					</td>
				</tr>
			</table>
		</form>
		<hr>
	</center>
</body>
</html>