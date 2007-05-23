<%@ tag display-name="text" dynamic-attributes="dyn" description="Displays an input text box." %>
<%@ attribute name="name" required="true"
              type="java.lang.String"
              description="This fields name." %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>

<w:row label="${name}">
<input type="text" name="${name}" value="${w:evaluate(name, pageContext)}" ${w:getAttributes(dyn)}/>
</w:row>
