<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메세지 리스트</title>
</head>
<body>
	<%-- 메세지 리스트, 밑에 메세지 입력 폼을 만들어주자. 
	메세지 리스트는 sevlet에서 이미 속성으로 만들어 줬다고 가정
	
	만약 속성 이름이 messageList  ==> ${messageList} (el 사용)
--%>
	<table border="1">
		<tr>
			<th>번호</th>
			<th>이름</th>
			<th>메세지</th>
		</tr>
		<c:forEach items="${viewData.messageList }" var="message">
			<tr>
				<td>${message.id }</td>
				<td>${message.name }</td>
				<td>${message.message }</td>
				<%-- 주소에 파라미터 담아서 보내기
				요청주소?파라미터이름=값
				form이 아니라 단순 페이지 이동을 할때도 파라미터를 같이 포함시켜서 이동
				이동한 다음 페이지에서 파라미터를 사용하기 위해
				<form action="confirmDelte" method="get">
					<input type="text" name="id" value="messge.id">
				</form>
			 --%>
				<td><button
						onclick="location.href='confirmDelete?id=${message.id}'">삭제</button></td>
			</tr>
		</c:forEach>
	</table>
	<hr>
	<%-- 여기에 페이지 네비게이션을 표시 --%>
	<%-- 처음 이전 1 2 3 4 5 6 7 8 9 10 다음 마지막 --%>
	<c:if test="${viewData.startPage != 1 }">
		<a href="messageList?page=1">[처음]</a>
		<a href="messageList?page=${viewData.startPage-1 }">[이전]</a>
	</c:if>
	
	<c:forEach var="pageNum" 
	begin="${viewData.startPage }" 
	end="${viewData.endPage < viewData.pageTotalCount ? viewData.endPage : viewData.pageTotalCount }">
		<c:choose>
		<%--현재 페이지인 경우 링크 비활성화 --%>
			<c:when test="${pageNum == viewData.currentPage }">
				<b>[${pageNum }]</b>
			</c:when>
		<%-- 다른 페이지로 넘어갈수 있도록 링크 제공 --%>
			<c:otherwise>
				<a href="messageList?page=${pageNum }">[${pageNum }]</a>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<c:if test="${viewData.endPage < viewData.pageTotalCount }">
		<a href="messageList?page=${viewData.endPage+1 }">[다음]</a>
		<a href="messageList?page=${viewData.pageTotalCount }">[마지막]</a>
	</c:if>
	
	<%-- write 요청을 보낼 form --%>
	<form action="write" method="post">
		<fieldset>
			<legend>메세지 입력</legend>
			<p>
				이름 : <input type="text" name="guestName">
			</p>
			<p>
				암호 : <input type="password" name="password">
			</p>
			<p>
				<textarea rows="3" cols="30" name="message"></textarea>
			</p>
			<input type="submit" value="메세지 저장">
		</fieldset>
	</form>
</body>
</html>