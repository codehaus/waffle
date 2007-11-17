<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;encoding=iso-8859-1" %>

<% 
	org.codehaus.waffle.taglib.acceptance.User user = new org.codehaus.waffle.taglib.acceptance.User();
	user.setPassword("54321");
	
	pageContext.setAttribute("senha","12345");
	pageContext.setAttribute("user",user);
%>

<html>
<head>
</head>
<body >

<div id="tag">
 
w:hidden: somente com atributo name  <br />

<w:hidden name="user.password" id="onlyName" />

<br /><br />

</div>



<div id="tag">

w:hidden: com atributo name e value <br />

<w:hidden name="user.password" value="${senha}" id="nameAndValue" />  <br /><br />

</div>



<div id="tag">

w:hidden: com rendered="true" <br />

<w:hidden name="user.password" rendered="true" id="renderedTrue"/>  <br /><br />

</div>



<div id="tag">

w:hidden: com rendered="false" <br />

<w:hidden name="user.password" rendered="false" id="renderedFalse"/> 

</div>

</body>
</html>
