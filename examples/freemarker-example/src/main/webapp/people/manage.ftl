<#import "/ftl/waffle/form.ftl" as w>
<?xml version="1.0" encoding="UTF-8"?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Select a person</title>  
</head>
<body>
<form action="${base}/people/manage.action" method="post">

    <h3>Waffle example: Select Person</h3>

    <table>
        <tr>
            <th>Id</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Date of Birth</th>
            <th>Skills</th>
            <th>Select</th>
        </tr>
        <#list people as person>
            <tr class="odd">
                <td>
                    <a href="${base}/people/manage.action?method=select|${person.id}">${person.id}</a>
                </td>
                <td>${person.firstName}</td>
                <td>${person.lastName}</td>
                <td>${person.email}</td>
                <td><#if person.dateOfBirth??>${person.dateOfBirth?string("dd/MM/yyyy")}</#if></td>
                <td><@w.asCSV person.getSkills()![] /> </p></td>
                <td><@w.checkbox "selectedIds" "${person.id}" /></td>
            </tr>
        </#list>
    </table>

    <a href="javascript:fireActionMethod('create');">New</a>; <a href="javascript:fireActionMethod('export');">Export Selected</a>

    <div id="showArea">
    	Selected people are:
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
</body>

</html>