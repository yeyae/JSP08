<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메세지 삭제 화면</title>
</head>
<body>
	<!-- 사용자에게 비밀번호를 입력받아서 비밀번호를 확인하는 요청 -->
	<h2>비밀번호 확인</h2>
	<form action="pwCheck" method="post">
		<input type="hidden" name="id" value="${param.id }"> 
		<!-- 이전 화면에서 주소와 동시에 넘겨준 파라미터를 사용
			사용자에게 보여줄 필요가 없는 값을 input type="hidden"
		 -->
		 비밀번호 : 
		 <input type="password" name="password">
		 <input type="submit" value="확인">
	</form>
</body>
</html>