<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.sist.model.*"%>
<jsp:useBean id="model" class="com.sist.model.Model"></jsp:useBean>
<%
	model.databoard_updateData(request);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<style type="text/css">
.row{
	margin:0px auto;
	width:700px;
}
</style>
</head>
<body>
	<div class="container">
		<h2 class="text-center">글쓰기</h2>
		<div class="row">
		<form method="post" action="update_ok.jsp">
			<table class="table">
				<tr>
					<th class="text-center info" width=15%>이름</th>
					<td width="85">
						<input type="text" name=name size=15 value="${vo.name }">
						<input type="hidden" name=no value="${vo.no }">
						<input type="hidden" name=page value="${curpage }">
					</td>
				</tr>
				<tr>
					<th class="text-center info" width=15%>제목</th>
					<td width="85">
						<input type="text" name=subject size=15 value="${vo.subject }">
					</td>
				</tr>
				<tr>
					<th class="text-center info" width=15%>내용</th>
					<td width="85">
						<textarea rows="10" cols="55" name="content">${vo.content }</textarea>
	
					</td>
				</tr>
				<tr>
					<th class="text-center info" width=15%>첨부파일</th>
					<td width="85">
						<input type="file" name=upload size=20>
					</td>
				</tr>
				<tr>
				<tr>
					<th class="text-center info" width=15%>비밀번호</th>
					<td width="85">
						<input type="password" name=pwd size=15>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="text-center">
						<input type="submit" class="btn btn-sm btn-primary" value="수정">
						<input type="button" class="btn btn-sm btn-danger" value="취소" onclick="javascript:history.back()">
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
</body>
</html>