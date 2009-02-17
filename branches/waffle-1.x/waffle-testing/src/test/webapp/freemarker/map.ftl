<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<form action="${base}/map.action" method="post">
	
	<h2>Map names</h2>
	
    <table>
        <#assign names=controller.names>
        <#list names.keySet() as name>
            <#assign value=names.get(name)>
            <tr>
                <td>${name},${value}</td>
            </tr>
        </#list>        
    </table>

</form>
</body>
</html>