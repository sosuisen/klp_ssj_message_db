<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="${mvc.basePath}/../app.css" rel="stylesheet">
<title>メッセージの累積</title>
</head>
<body>
	${loginUser.name}さん、こんにちは！<br>
	<form action="list" method="POST">
		メッセージ：<input type="text" name="message">
		<button>送信</button>
	</form>

	<form action="clear" method="GET">
		<button>Clear</button>
	</form>
	<hr>
	<h1>メッセージ一覧</h1>
	<c:forEach var="mes" items="${messages}">
		<div>${mes.name}:${mes.message}
	</c:forEach>
	<p>
		<a href="${mvc.basePath}/login">ログアウト</a>
	</p>
	<p>
		<a href="${mvc.basePath}/">ホームへ戻る</a>
	</p>
</body>
</html>
