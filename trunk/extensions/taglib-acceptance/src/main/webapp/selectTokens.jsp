<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>

<html>
<head><title>SelectTokensTest</title></head>
<body >
 
<fmt:setLocale value="pt_Br" />

<div id="renderedTest">
	<w:selectTokens id="notRendered" rendered="${false}" var="day" name="notRendered" tokens="first,1,second,2,third,3,fourth,4,fifth,5,sixth,6,seventh,7">${day}</w:selectTokens>
  <w:selectTokens id="rendered" rendered="${true}" var="day" name="rendered" tokens="first,1,second,2,third,3,fourth,4,fifth,5,sixth,6,seventh,7">${day}</w:selectTokens>
</div>

test selected=2 <br>
<w:selectTokens var="day" name="selected.2" tokens="first,1,second,2,third,3,fourth,4,fifth,5,sixth,6,seventh,7" selected="second">${day}</w:selectTokens>
<br>
<br>

w:select type="combo": <br>
<w:selectTokens var="day" name="combo" tokens="first,1,second,2,third,3,fourth,4,fifth,5,sixth,6,seventh,7" selected="third">${day}</w:selectTokens>

<br>
<br>

w:selectTokens type="radio": <br>
<w:selectTokens type="radio" var="day" name="radio" tokens="first,1,second,2,third,3,fourth,4,fifth,5,sixth,6,seventh,7" >${day}</w:selectTokens>

</body >
</html>
