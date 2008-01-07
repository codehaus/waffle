<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<html>
<head><title>Errors tag acceptance test</title></head>
<body>
<h1>Errors tag test:</h1>
<h2>All errors:</h2>
<ul id="allErrors">
  <w:errors>
    <li class="error">${error.message}</li>
  </w:errors>
</ul>

<w:form id="productForm" type="table" action="products.waffle" method="post">
  <w:hidden name="method" value="add"/>
  <w:text id="nameField" name="name"/>
  <w:number id="priceField" name="price"/>
  <w:submit/>
</w:form>

<h2>Field errors:</h2>
<ul id="nameErrors">
  <w:errors field="name">
    <li class="error">
      field: ${error.name}<br/>
      value: ${error.value}<br/>
      message: ${error.message}
    </li>
  </w:errors>
</ul>

<ul id="priceErrors">
  <w:errors field="price">
    <li class="error">
      field: ${error.name}<br/>
      value: ${error.value}<br/>
      message: ${error.message}
    </li>
  </w:errors>
</ul>

<h1>Messages tag test:</h1>
<p><a id="showMessagesAction" href="products.waffle?method=showMessages">Show Messages</a></p>
<ul id="messages">
  <w:messages>
    <li>${message}</li>
  </w:messages>
</ul>

<p id="successMessage"><w:message key="success"/></p>
<p id="failureMessage"><w:message key="failure"/></p>

</body>
</html>
