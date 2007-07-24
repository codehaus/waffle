<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>
<%@ page import="java.lang.Number" %>

<html>
<head>
</head>
<body >
 
<%
	pageContext.setAttribute("number",new Double(1234.567));
  pageContext.setAttribute("number2",new Double(1234.5));
%>

<fmt:setLocale value="pt_BR" />

Number Tag:
<br/>
without pattern <w:number id="asfsad" value="${number}" name="numberLocalized"/>
<br/>
with pattern <w:number name="d" value="${number2}" pattern="00,000.##"/>
<br/>
only name atribute:<w:number name="number" />
<br/>
<br/>
</body >
</html>
