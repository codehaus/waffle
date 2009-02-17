<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="errors" type="org.codehaus.waffle.validation.ErrorsContext" required="false" %>
<%@ attribute name="field" required="false" %>
<%@ variable name-given="error" scope="NESTED" %>

<c:if test="${empty errors }">
  <c:set var="errors" value="${requestScope.errors}"/>
</c:if>
<c:if test="${not empty errors}">
  <c:choose>
    <c:when test="${empty field}">
      <c:set var="errorMessages"
             value="${errors.allErrorMessages }"/>
    </c:when>
    <c:otherwise>
      <c:set var="errorMessages"
             value="${w:findErrorsForField(errors, 'FIELD', field) }"/>
    </c:otherwise>
  </c:choose>
  <c:if test="${fn:length(errorMessages) ne 0 }">
    <c:forEach var="error" items="${errorMessages}">
      <jsp:doBody/>
    </c:forEach>
  </c:if>
</c:if>
