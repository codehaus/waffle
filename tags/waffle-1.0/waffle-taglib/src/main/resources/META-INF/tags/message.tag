<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="messages" type="org.codehaus.waffle.i18n.MessagesContext" required="false" %>
<%@ attribute name="key" required="true" %>

<c:if test="${empty messages}">
  <c:set var="messages" value="${requestScope.messages}"/>
</c:if>
<c:if test="${not empty messages}">
  ${w:findMessageForKey(messages, key)}
</c:if>
