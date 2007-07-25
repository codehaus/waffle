<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>

<html>
<head>
</head>
<body >
 
<%
  pageContext.setAttribute("number", new Double(1234.5));
%>

<fmt:setLocale value="${param.locale}" />

Number Tag:
<br/>
without pattern <w:number id="localizedNumber" name="number" />
<br/>
with pattern <w:number id="patternNumber" name="number" pattern="00,000.00" />
<br/>
<br/>

</body >
</html>
