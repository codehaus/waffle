<#import "/ftl/waffle/i18n.ftl" as i>
<#import "/ftl/waffle/form.ftl" as w>
<#import "/ftl/waffle/validation.ftl" as v>
<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@i.messageFor "manageTitle" "Select Person"/></title>  
</head>
<body>
<#include "/navigation.ftl" parse="true">
<div id="content">
<form action="${base}/people/manage.action" method="post">

    <h3><@i.messageFor "manageTitle" "Select Person"/></h3>

   <@v.errorsAsUl errors "true" "false" "class='errors'"/>
   
    <table>
        <tr>
            <th>Id</th>
            <th><@i.messageFor "firstName" "First Name"/></th>
            <th><@i.messageFor "lastName" "Last Name"/></th>
            <th><@i.messageFor "email" "Email"/></th>
            <th><@i.messageFor "dateOfBirth" "Date of Birth"/></th>
            <th><@i.messageFor "type" "Type"/></th>
            <th><@i.messageFor "skills" "Skills"/></th>
            <th><@i.messageFor "select" "Select"/></th>
        </tr>
        <#list people as person>
            <tr class="odd">
                <td>
                    <a href="${base}/people/manage.action?method=select&id=${person.id}">${person.id}</a>
                </td>
                <td>${person.firstName}</td>
                <td>${person.lastName}</td>
                <td>${person.email}</td>
                <td><#if person.dateOfBirth??>${person.dateOfBirth?string("dd/MM/yyyy")}</#if></td>
                <td>${person.type}</td>
                <td><@w.asCSV person.getSkills()![] /> </p></td>
                <td><@w.checkbox "selectedIds" "${person.id}" /></td>
            </tr>
        </#list>
    </table>

    <a href="javascript:fireActionMethod('create');"><@i.messageFor "create" "New"/></a>; <a href="javascript:fireActionMethod('export');"><@i.messageFor "export" "Export"/></a>;
    <br/>
    <a href="javascript:fireActionMethod('inexistant');"><@i.messageFor "inexistant" "Inexistant method"/></a>;
    <a href="javascript:fireActionMethod('failing');"><@i.messageFor "failing" "Failing method"/></a>

    <div id="showArea">    	
        <table>
        <#list selectedPeople as person>
	        <tr class="odd">
                <td>${person.firstName} ${person.lastName} (${person.id})</td>
            </tr>
        </#list>
        </table>
	</div>

    <!-- Example of partial page -->
    <div id="editArea">
      <#if person?exists>
        <#include "edit.ftl" parse="true">
      </#if>
    </div>

</form>
</div>
</body>

</html>