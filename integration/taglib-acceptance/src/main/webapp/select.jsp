<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;encoding=iso-8859-1" %>
<%@ page import="java.util.Date,java.util.*,org.codehaus.waffle.taglib.acceptance.Product" %>

<jsp:useBean class="org.codehaus.waffle.taglib.acceptance.Product" id="Produto" />

<html>
<head><title>SelectTest</title></head>
<body >
 
<% 
	List<Product> lista = new ArrayList<Product>();
	lista.add(new Product(1,"trabant"));
	lista.add(new Product(2,"wartburg"));
	lista.add(new Product(3,"skoda"));
	lista.add(new Product(null, "nasty"));
	pageContext.setAttribute("lista",lista);
%>

<fmt:setLocale value="pt_Br" />

<div id="renderedTest">
	<w:select id="notRendered" rendered="${false}" var="produto" name="notRendered" items="${lista}" value="id">${produto.name}</w:select>
	<w:select id="rendered" rendered="${true}" var="produto" name="rendered" items="${lista}" value="id">${produto.name}</w:select>
</div>

test selected=2 <br />
<w:select var="produto" name="produto.id.selected.2" items="${lista}" value="id" selected="${2}">
        ${produto.name}
</w:select>

<br />
<br />

test selected=null <br />
<w:select var="produto" name="produto.id.selected.null" items="${lista}" value="id" selected="${null}">
        ${produto.name}
</w:select>

<br />
<br />

w:select type="combo": <br />
<w:select var="produto" name="produto.id" items="${lista}" value="id" selected="${2}" addEmpty="true">
        ${produto.name}
</w:select>

<br />
<br />

w:select type="radio": <br />
<w:select type="radio" var="produto" name="produto.id" items="${lista}" value="id" >
        ${produto.name}
</w:select>

<br /><br />
w:select with null list: <br />
<w:select var="produto" name="nulledList" items="${unknown}" value="id" >
        ${produto.name}
</w:select>

</body >
</html>
