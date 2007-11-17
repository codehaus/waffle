<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.Date,java.util.List" %>
<%@ page contentType="text/html;encoding=iso-8859-1" %>

<html>
<head></head>
<body >
 
<% 
	Date d = new Date();
	pageContext.setAttribute("date",d);
%>

<fmt:setLocale value="pt_Br" />


Time Tag:
<br/>
com timeStyle <w:time name="d"  value="${date}" timeStyle="medium"/>
<br/>
com pattern <w:time name="d"  value="${date}" pattern="hh:mm"/>
<br/>
s� name atribute: <w:time name="date" />
<br/>
<br/>

INPUT+FMT:
<input value="<fmt:formatDate value="${date}" type="time" timeStyle="medium"/>" />

</body >
</html>
