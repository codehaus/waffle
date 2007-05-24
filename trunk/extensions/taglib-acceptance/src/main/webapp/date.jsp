<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>
<%@ page import="java.util.Date" %>

<html>
<head>
</head>
<body >
 
<% 
	Date date = new Date();
	pageContext.setAttribute("date",date);
%>

<fmt:setLocale value="pt_Br" />

Date Tag:
<br/>
com dateStyle <w:date id="asfsad" name="date" dateStyle="short"/>
<br/>
com pattern <w:date name="d"  value="${date}" pattern="dd/MM/yyyy"/>
<br/>
s� name atribute:<w:date name="date" />
<br/>
<br/>

INPUT+FMT:
<input value="<fmt:formatDate value="${date}" type="date" dateStyle="long" />" />

</body >
</html>
