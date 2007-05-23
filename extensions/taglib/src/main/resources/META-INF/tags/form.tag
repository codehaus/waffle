<%@ tag import="org.waffle.taglib.form.FormStyle" %>
<%@ tag import="org.waffle.taglib.form.TableStyle" %>
<%@ tag import="java.util.Map" %>
<%@ tag display-name="text" dynamic-attributes="dyn" description="Displays a form." %>
<%@ attribute name="action" required="false"
              type="java.lang.String"
              description="The url to link this form to." %>
<%@ variable variable-class="org.waffle.taglib.form.FormStyle" name-given="formStyle" scope="NESTED" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>

<form ${not empty action ? 'action="' + action + '"' : ''}>
    <%
        FormStyle style = new TableStyle((Map) jspContext.getAttribute("dyn"), out);
        request.setAttribute("formStyle", style);
    %>
    ${w:formStyle("beginForm", formStyle)}
    <jsp:doBody />
    ${w:formStyle("finishForm", formStyle)}
</form>