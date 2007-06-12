<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>
<%@ page import="java.util.Date,java.util.*" %>

<html>
<head>

<title>DateTimePickerTest</title>

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

<div id="dateTimePickerTest">
<w:dateTimePicker name="birthday" value="${birthDay}" pattern="dd/MM/yyyy" />
</div>


<div id="dateTimePickerTest2">
<w:dateTimePicker name="register" value="${register}" pattern="yyyy-MM-dd" />
</div>

</body >
</html>
