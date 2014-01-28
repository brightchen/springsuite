<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
  <title>Home Page</title>
</head>
<body>
  <div id="menuDiv"  class="menuDiv">
    <ul id="menuRoot" class="menuRoot">
      <li>
        <a href='javascript:void(0);' class='cat_close category'>&nbsp;</a>
        <a href='javascript:void(0);'>Diabeties</a>
      </li>
    </ul>
    <ul id='c_13'>
      <li>
        <a href='javascript:void(0);' class='product'>&nbsp;</a>
        <a href='javascript:void(0);'>Skelaxin</a>
      </li>
    </ul>
        
  </div>   
  
  <p>Display image now</p>
  <img src="<%= request.getContextPath() %>/resources/img/img1.jpg" alt="img1.jpg" />


  <iframe id="content" src="<%= request.getContextPath() %>/resources/html/content.html" width="500" >
  </iframe>

</body>
</html>