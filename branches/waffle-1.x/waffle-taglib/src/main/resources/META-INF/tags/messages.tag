<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="messages" type="org.codehaus.waffle.i18n.MessagesContext" required="false" %>
<%@ variable name-given="message" scope="NESTED" %>

<c:if test="${empty messages}">
  <c:set var="messages" value="${requestScope.messages}"/>
</c:if>
<c:if test="${not empty messages}">
  <c:if test="${messages.messageCount gt 0}">
    <c:forEach var="message" items="${messages.messages}">
      <jsp:doBody/>
    </c:forEach>
  </c:if>
</c:if>
