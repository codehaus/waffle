<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;encoding=iso-8859-1" %>

<% 
	pageContext.setAttribute("user","Nico");
%>

<html>
<head>
</head>
<body >
 
w:checkbox checked="true": <br>

<w:checkbox name="ativo" checked="true" id="checkedTrue" />

<br/><br/>

w:checkbox checked="false": <br>

<w:checkbox name="ativo" checked="false" id="checkedFalse" />

<br/><br/>

w:checkbox sem checked: <br>

<w:checkbox name="ativo" id="noChecked" /> 

</body>
</html>
