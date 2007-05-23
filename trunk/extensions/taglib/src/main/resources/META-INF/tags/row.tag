<%@ tag display-name="row" dynamic-attributes="dyn" description="Displays a row." import="org.waffle.taglib.form.NullStyle"%>
<%@ attribute name="label" required="false"
              type="java.lang.String"
              description="The label to use (i18n based)." %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>

${w:formStyle("beginLine", formStyle)}
<c:choose>
    <c:when test="${not empty label}">
        <w:column><fmt:message key="${label}" /></w:column>
        <w:column><jsp:doBody /></w:column>
        <%-- <w:errors path="${errorsPath}" /> --%>
    </c:when>
    <c:otherwise>
        <w:column><jsp:doBody /></w:column>
    </c:otherwise>
</c:choose>
${w:formStyle("finishLine", formStyle)}