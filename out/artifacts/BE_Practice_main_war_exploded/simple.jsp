<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>도서 관리</title>
</head>
<body>
<h2>도서 관리</h2>
<form action="main" accept-charset="utf-8">
  <fieldset style="width: 150">
    <input type="hidden" name="action" value="regist" method="post"/>
    <legend>도서 관리</legend>
    <div><label for="isbn">도서번호</label> <input type="text" name="isbn" id="isbn"/></div>
    <div><label for="title">도서명</label> <input type="text" name="title" id="title"/></div>
    <div><label for="author">저자</label> <input type="text" name="author" id="author"/></div>
    <div><label for="price">가격</label> <input type="text" name="price" id="price"/></div>
    <div><label for="desc">설명</label> <input type="text" name="desc" id="desc"/></div>
    <div>
      <button id="registBtn">등록</button>
      <button id="cancelBtn">취소</button>
    </div>
  </fieldset>
</form>
</body>
</html>