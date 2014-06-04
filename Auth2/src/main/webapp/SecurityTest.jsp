<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="rest/test/sget" method="get" name="gettest">
		<input type="submit" value="GET TEST"/>
	</form>
	<form action="rest/test/spost" method="post" name="posttest">
		<input type="submit" value="POST TEST"/>
	</form>
	<form action="rest/test/sput" method="put" name="puttest">
		<input type="submit" value = "PUT TEST"/>
	</form>
</body>
</html>