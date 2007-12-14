<%@ taglib uri="http://waffle.codehaus.org" prefix="w" %>
<html>
<head><title>Errors tag acceptance test</title></head>
<body>
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

</body>
</html>