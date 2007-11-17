<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>
<%@ page import="java.util.Date,java.util.*" %>

<html>
<head>

<title>CalendarTest</title>

<link rel="stylesheet" type="text/css" media="all" href="js/calendar-brown.css">

<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/calendar-en.js"></script>
<script type="text/javascript" src="js/calendar-setup.js"></script>

</head>
<body >
 
<% 
	pageContext.setAttribute("birthDay",new Date());
	pageContext.setAttribute("register",new Date());
%>

<div id="calendarTest">
<w:calendar name="birthday" value="${birthDay}" pattern="dd/MM/yyyy" />
</div>

<w:form id="meu_form" type="table" action="" >

<div id="calendarTest2">
<w:calendar name="register" value="${register}" pattern="yyyy-MM-dd" />
</div>

<div id="calendarTest3">
<w:calendar label="testLabel" name="register" value="${register}" pattern="dd.MM.yyyy" />
</div>

</w:form>
</body >
</html>
