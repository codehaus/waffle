<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="java.util.Date,java.util.*,org.waffle.acceptance.Product" %>
<%@ page contentType="text/html;encoding=iso-8859-1" %>

<jsp:useBean class="org.waffle.acceptance.Product" id="Produto" />

<html>
<head>
</head>
<body >

<fmt:setLocale value="pt_Br" />

<div id="renderedTest">
	<w:form type="table" rendered="${false}" id="notRendered" action="none"/>
	<w:form type="table" rendered="${true}" id="rendered" action="none"/>
</div>
 
<% 
	String nome = "Marco"; 
	Date d = new Date();
	pageContext.setAttribute("date",d);
	pageContext.setAttribute("nome",nome);
%>

Form Tag:
<br/>
<w:form id="meu_form" type="table" action="" >

<w:text id="mmmm" name="nome" />
<w:date name="date"   pattern="hh:mm"/>

</w:form>

</body >
</html>
