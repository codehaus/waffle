<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<form action="${base}/list.action" method="post">
	
	<h2>List names</h2>
	
    <table>
        <#list controller.names as name>
            <tr>
                <td>${name}</td>
            </tr>
        </#list>        
    </table>

</form>
</body>
</html>